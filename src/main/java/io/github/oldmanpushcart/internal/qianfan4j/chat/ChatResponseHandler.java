package io.github.oldmanpushcart.internal.qianfan4j.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.oldmanpushcart.internal.qianfan4j.util.JacksonUtils;
import io.github.oldmanpushcart.internal.qianfan4j.util.StringUtils;
import io.github.oldmanpushcart.qianfan4j.QianFanClient;
import io.github.oldmanpushcart.qianfan4j.chat.ChatRequest;
import io.github.oldmanpushcart.qianfan4j.chat.ChatResponse;
import io.github.oldmanpushcart.qianfan4j.chat.FunctionCall;
import io.github.oldmanpushcart.qianfan4j.chat.NotSafeChatResponseException;
import io.github.oldmanpushcart.qianfan4j.chat.function.ChatFn;
import io.github.oldmanpushcart.qianfan4j.chat.function.ChatFunction;
import io.github.oldmanpushcart.qianfan4j.chat.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

import static io.github.oldmanpushcart.internal.qianfan4j.util.JacksonUtils.compact;
import static io.github.oldmanpushcart.qianfan4j.Constants.LOGGER_NAME;
import static io.github.oldmanpushcart.qianfan4j.chat.message.Message.ofFunction;
import static io.github.oldmanpushcart.qianfan4j.chat.message.Message.ofFunctionCall;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class ChatResponseHandler implements Function<ChatResponse, CompletionStage<ChatResponse>> {

    private final static ObjectMapper mapper = JacksonUtils.mapper();
    private final static Logger logger = LoggerFactory.getLogger(LOGGER_NAME);

    // 任务拆解匹配
    private static final Pattern taskSplitPattern = Pattern.compile("任务拆解:\\[(.*?)]\\.");
    private static final Pattern subTaskSplitPattern = Pattern.compile("\\[(.*?)]");

    private final QianFanClient client;
    private final ChatRequest request;
    private final Consumer<ChatResponse> consumer;

    public ChatResponseHandler(QianFanClient client, ChatRequest request, Consumer<ChatResponse> consumer) {
        this.client = client;
        this.request = request;
        this.consumer = consumer;
    }

    @Override
    public CompletionStage<ChatResponse> apply(ChatResponse response) {

        // 检查对话返回是否安全
        if (!response.isSafe()) {
            throw new NotSafeChatResponseException(response);
        }

        // 处理函数调用
        if (response.isFunctionCall()) {
            return handingFunctionCall(response);
        }

        return completedFuture(response)
                .thenApply(v -> {
                    request.messages().add(Message.ofAi(v.content()));
                    return v;
                });
    }

    // 处理函数调用
    private CompletableFuture<ChatResponse> handingFunctionCall(ChatResponse response) {
        final var call = response.call();
        final var queue = parseTaskQueue(call.thoughts());

        // 第一个任务一定是当前正在执行的FunctionCall，所以这里可以直接弹栈
        queue.poll();
        if (logger.isDebugEnabled()) {
            logger.debug("{}/function <= {}", request, compact(mapper, call.arguments()));
        }

        // 定位到函数
        final var function = request.functions().stream()
                .filter(fn -> call.name().equals(fn.getClass().getAnnotation(ChatFn.class).name()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("function: %s not found!".formatted(call.name())));

        // 执行函数调用
        return calling(function, call)
                .thenCompose(resultJson -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug("{}/function => {}", request, compact(mapper, resultJson));
                    }
                    final var fnRequest = ChatRequest.newBuilder(request)
                            .messages(ofFunctionCall(call), ofFunction(call.name(), resultJson))
                            .functions(true, function)
                            .build();
                    return client.chat(fnRequest).stream(consumer)
                            .thenCompose(v -> executeTask(v, queue));
                });
    }

    // 函数调用
    private CompletableFuture<String> calling(ChatFunction<?, ?> function, FunctionCall call) {
        try {

            // 解析元数据
            final var meta = ChatFunctionMeta.of(function.getClass());

            // 执行函数
            return function.call(JacksonUtils.toObject(mapper, meta.parameter().type(), call.arguments()))
                    .thenApply(result -> JacksonUtils.toJson(mapper, result));

        } catch (Throwable cause) {
            throw new RuntimeException("function: %s call error!".formatted(call.name()), cause);
        }

    }


    // 解析思考过程中的任务拆解
    private static Queue<String> parseTaskQueue(String thought) {

        final var queue = new LinkedList<String>();
        final var dbcThought = StringUtils.toDBC(null != thought ? thought : "call function");

        // 如果没有任务拆解，则当前思考就是任务本身
        if (!dbcThought.contains("任务拆解:")) {
            queue.push(thought);
            return queue;
        }

        // 如果存在任务拆解，则进行任务拆解
        final var matcher = taskSplitPattern.matcher(dbcThought);
        if (matcher.find()) {
            final var group = matcher.group();
            Arrays.stream(group.substring(group.indexOf('[') + 1, group.lastIndexOf(']')).split(","))
                    .map(taskKv -> taskKv.split(":"))
                    .filter(taskSegments -> taskSegments.length == 2)
                    .map(taskSegments -> taskSegments[1])
                    .forEach(queue::add);
        }

        return queue;
    }

    // 执行子任务
    private CompletableFuture<ChatResponse> executeTask(ChatResponse response, Queue<String> queue) {
        if (queue.isEmpty()) {
            return completedFuture(response);
        }
        final var task = queue.poll();
        final var taskRequest = ChatRequest.newBuilder(request)
                .messages(Message.ofUser(task))
                .functions(true, parseSubTaskFunctionList(request.functions(), task))
                .build();
        return client.chat(taskRequest).stream(consumer)
                .thenCompose(taskResponse -> executeTask(taskResponse, queue));
    }

    // 解析子任务函数集合
    private static ChatFunction<?, ?>[] parseSubTaskFunctionList(List<ChatFunction<?, ?>> functions, String task) {
        final var names = new HashSet<String>();
        final var matcher = subTaskSplitPattern.matcher(task);
        while (matcher.find()) {
            final var group = matcher.group();
            final var name = group.substring(group.indexOf('[') + 1, group.lastIndexOf(']'));
            names.add(name);
        }
        return functions.stream()
                .filter(fn -> names.contains(fn.getClass().getAnnotation(ChatFn.class).name()))
                .toArray(ChatFunction<?, ?>[]::new);
    }

}
