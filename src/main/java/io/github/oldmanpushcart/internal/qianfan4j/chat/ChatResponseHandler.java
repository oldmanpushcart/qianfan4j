package io.github.oldmanpushcart.internal.qianfan4j.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.oldmanpushcart.internal.qianfan4j.base.api.ApiExecutor;
import io.github.oldmanpushcart.internal.qianfan4j.util.JacksonUtils;
import io.github.oldmanpushcart.internal.qianfan4j.util.StringUtils;
import io.github.oldmanpushcart.qianfan4j.chat.ChatRequest;
import io.github.oldmanpushcart.qianfan4j.chat.ChatResponse;
import io.github.oldmanpushcart.qianfan4j.chat.ChatResponseNotSafeException;
import io.github.oldmanpushcart.qianfan4j.chat.function.ChatFunctionKit;
import io.github.oldmanpushcart.qianfan4j.chat.message.Message;
import io.github.oldmanpushcart.qianfan4j.util.Aggregatable;
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

    // 子任务拆解匹配
    private static final Pattern subTaskSplitPattern = Pattern.compile("\\[(.*?)]");

    private final ApiExecutor executor;
    private final ChatRequest request;
    private final Consumer<ChatResponse> consumer;

    public ChatResponseHandler(ApiExecutor executor, ChatRequest request, Consumer<ChatResponse> consumer) {
        this.executor = executor;
        this.request = request;
        this.consumer = consumer;
    }

    @Override
    public CompletionStage<ChatResponse> apply(ChatResponse response) {

        // 检查对话返回是否安全
        if (!response.isSafe()) {
            throw new ChatResponseNotSafeException(response);
        }

        return response.isFunctionCall()
                ? handingFunctionCall(response)
                : completedFuture(response);
    }

    // 处理函数调用
    private CompletableFuture<ChatResponse> handingFunctionCall(ChatResponse response) {
        final var call = response.call();
        final var stub = request.kit().require(call.name());

        // 上下文
        final var ctx = new Ctx(parseTaskQueue(call.thoughts()));

        // 第一个任务一定是当前正在执行的FunctionCall，所以这里可以直接弹栈
        ctx.queue.poll();
        if (logger.isDebugEnabled()) {
            logger.debug("{}/function <= {}", request, compact(mapper, call.arguments()));
        }

        // 执行函数调用
        return stub.call(call.arguments())
                .thenCompose(resultJson -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug("{}/function => {}", request, compact(mapper, resultJson));
                    }
                    final var fnRequest = ChatRequest.newBuilder(request)
                            .replaceKit(request.kit().sub(call.name()))
                            .messages(ofFunctionCall(call), ofFunction(call.name(), resultJson))
                            .build();
                    return executor.execute(fnRequest, Aggregatable::aggregate, consumer)
                            .thenCompose(v -> executeTask(v, ctx));
                });
    }

    private record Ctx(List<Message> history, Queue<String> queue) {

        public Ctx(Queue<String> queue) {
            this(new ArrayList<>(), queue);
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
    private CompletableFuture<ChatResponse> executeTask(ChatResponse response, Ctx ctx) {
        if (ctx.queue.isEmpty()) {
            return completedFuture(response);
        }
        final var task = ctx.queue.poll();
        ctx.history.add(Message.ofUser(task));

        final var taskRequest = ChatRequest.newBuilder(request)
                .replaceKit(parseSubTaskFunctionKit(request.kit(), task))
                .messages(ctx.history)
                .build();

        return executor.execute(taskRequest, Aggregatable::aggregate, consumer)
                .thenCompose(taskResponse -> executeTask(taskResponse, ctx))
                .thenApply(v-> {
                    ctx.history.add(Message.ofAi(v.result()));
                    return v;
                });
    }

    // 解析子任务函数集合
    private static ChatFunctionKit parseSubTaskFunctionKit(ChatFunctionKit kit, String task) {
        final var names = new LinkedList<String>();
        final var matcher = subTaskSplitPattern.matcher(task);
        while (matcher.find()) {
            final var group = matcher.group();
            final var name = group.substring(group.indexOf('[') + 1, group.lastIndexOf(']'));
            names.add(name);
        }
        return kit.sub(names.toArray(String[]::new));
    }


}
