package io.github.ompc.erniebot4j.chat.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ompc.erniebot4j.chat.ChatRequest;
import io.github.ompc.erniebot4j.chat.ChatResponse;
import io.github.ompc.erniebot4j.chat.function.ChatFunctionKit;
import io.github.ompc.erniebot4j.chat.message.Message;
import io.github.ompc.erniebot4j.util.JacksonUtils;
import io.github.ompc.erniebot4j.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

import static io.github.ompc.erniebot4j.util.JacksonUtils.compact;
import static java.util.concurrent.CompletableFuture.completedFuture;

class ChatResponseHandler implements Function<ChatResponse, CompletionStage<ChatResponse>> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ObjectMapper mapper = JacksonUtils.mapper();
    private final Merged merged;
    private final ChatExecutor executor;
    private final HttpClient http;
    private final ChatRequest request;
    private final Consumer<ChatResponse> consumer;

    public ChatResponseHandler(Merged merged, ChatExecutor executor, HttpClient http, ChatRequest request, Consumer<ChatResponse> consumer) {
        this.merged = merged;
        this.executor = executor;
        this.http = http;
        this.request = request;
        this.consumer = consumer;
    }

    @Override
    public CompletionStage<ChatResponse> apply(ChatResponse response) {

        // 合并调用量
        merged.merge(response);

        // 处理函数调用
        if (response.isFunctionCall()) {
            return handingFunctionCall(response);
        }

        // 处理普通返回
        request.messages().add(Message.bot(response.sentence().content()));
        return completedFuture(response);
    }

    private CompletableFuture<ChatResponse> handingFunctionCall(ChatResponse response) {
        final var call = response.call();
        final var stub = request.kit().require(call.name());
        final var queue = parseTaskQueue(call.thoughts());

        // 第一个任务一定是当前正在执行的FunctionCall，所以这里可以直接弹栈
        queue.poll();

        if (logger.isDebugEnabled()) {
            logger.debug("erniebot://chat/{}/function <= {}", request.model().name(), compact(mapper, call.arguments()));
        }
        return stub.call(call.arguments())
                .thenCompose(resultJson -> {
                    if (logger.isDebugEnabled()) {
                        logger.debug("erniebot://chat/{}/function => {}", request.model().name(), compact(mapper, resultJson));
                    }
                    final var fcMessage = Message.functionCall(call.name(), call.arguments());
                    final var frMessage = Message.functionResult(call.name(), resultJson);
                    request.messages().add(fcMessage);
                    request.messages().add(frMessage);
                    return executor.execute(merged, http, request, consumer)
                            .whenComplete((v, ex) -> {
                                request.messages().remove(fcMessage);
                                request.messages().remove(frMessage);
                            })
                            .thenCompose(v -> executeTask(v, queue));
                });
    }

    /**
     * 解析思考过程中的任务拆解
     *
     * @param thought 思考过程
     * @return 任务拆解队列
     */
    private static Queue<String> parseTaskQueue(String thought) {

        final var queue = new LinkedList<String>();
        final var dbcThought = StringUtils.toDBC(null != thought ? thought : "call function");

        // 如果没有任务拆解，则当前思考就是任务本身
        if (!dbcThought.contains("任务拆解:")) {
            queue.push(thought);
            return queue;
        }

        // 如果存在任务拆解，则进行任务拆解
        final var pattern = Pattern.compile("任务拆解:\\[(.*?)]\\.");
        final var matcher = pattern.matcher(dbcThought);
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
        final var taskRequest = new ChatRequest.Builder(request)
                .replaceKit(parseSubTaskFunctionKit(request.kit(), task))
                .message(Message.human(task))
                .build();

        return executor.execute(merged, http, taskRequest, consumer)
                .thenCompose(taskResponse -> executeTask(taskResponse, queue));
    }

    // 解析子任务函数集合
    private static ChatFunctionKit parseSubTaskFunctionKit(ChatFunctionKit kit, String task) {
        final var names = new LinkedList<String>();
        final var pattern = Pattern.compile("\\[(.*?)]");
        final var matcher = pattern.matcher(task);
        while (matcher.find()) {
            final var group = matcher.group();
            final var name = group.substring(group.indexOf('[') + 1, group.lastIndexOf(']'));
            names.add(name);
        }
        return kit.sub(names.toArray(String[]::new));
    }

}
