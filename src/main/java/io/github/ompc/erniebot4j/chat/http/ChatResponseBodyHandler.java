package io.github.ompc.erniebot4j.chat.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.ompc.erniebot4j.chat.ChatResponse;
import io.github.ompc.erniebot4j.executor.http.HttpContentType;
import io.github.ompc.erniebot4j.executor.Model;
import io.github.ompc.erniebot4j.util.FeatureDetection;
import io.github.ompc.erniebot4j.util.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

class ChatResponseBodyHandler implements HttpResponse.BodyHandler<ChatResponse> {

    private static final Logger logger = LoggerFactory.getLogger(ChatResponseBodyHandler.class);
    private static final ObjectMapper mapper = JacksonUtils.mapper()
            .registerModule(new SimpleModule() {{
                addDeserializer(ChatResponse.class, new ChatResponseJsonDeserializer());
            }});


    private final Consumer<ChatResponse> consumer;
    private final Model model;

    public ChatResponseBodyHandler(Model model, Consumer<ChatResponse> consumer) {
        this.model = model;
        this.consumer = consumer;
    }

    @Override
    public HttpResponse.BodySubscriber<ChatResponse> apply(HttpResponse.ResponseInfo info) {
        final var ct = HttpContentType.parse(info.headers());
        final var charset = ct.charset();
        return switch (ct.mime()) {
            case HttpContentType.MIME_APPLICATION_JSON -> new BlockBodySubscriber(model, charset, consumer);
            case HttpContentType.MIME_TEXT_EVENT_STREAM -> new StreamBodySubscriber(model, charset, consumer);
            default -> throw new RuntimeException("unsupported response http Content-Type: %s".formatted(ct.mime()));
        };
    }

    private static ChatResponse toChatResponse(String json) {
        final var node = JacksonUtils.toNode(mapper, json);
        // 处理返回错误
        if (node.has("error_code")) {
            throw new RuntimeException("response error: %s; %s".formatted(
                    node.get("error_code").asInt(),
                    node.has("error_msg")
                            ? node.get("error_msg").asText()
                            : null
            ));
        }

        // 检查是否安全
        if (node.has("need_clear_history") && node.get("need_clear_history").asBoolean()) {
            throw new RuntimeException("response is not safe! ban=%s".formatted(
                    node.get("ban_round").asInt()
            ));
        }

        return JacksonUtils.toObject(mapper, ChatResponse.class, node);
    }

    private static class BlockBodySubscriber implements HttpResponse.BodySubscriber<ChatResponse> {

        private final Model model;
        private final Charset charset;
        private final Consumer<ChatResponse> consumer;
        private final CompletableFuture<ChatResponse> future = new CompletableFuture<>();
        private final ByteArrayOutputStream output = new ByteArrayOutputStream();
        private final byte[] bytes = new byte[10240];

        private BlockBodySubscriber(Model model, Charset charset, Consumer<ChatResponse> consumer) {
            this.model = model;
            this.charset = charset;
            this.consumer = consumer;
        }

        @Override
        public CompletionStage<ChatResponse> getBody() {
            return future;
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            subscription.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(List<ByteBuffer> buffers) {
            synchronized (bytes) {
                buffers.forEach(buffer -> {
                    while (buffer.hasRemaining()) {
                        final var length = Math.min(buffer.remaining(), bytes.length);
                        buffer.get(bytes, 0, length);
                        output.write(bytes, 0, length);
                    }
                });
            }
        }

        @Override
        public void onError(Throwable ex) {
            future.completeExceptionally(ex);
        }

        @Override
        public void onComplete() {
            try {
                final var body = output.toString(charset);
                logger.debug("erniebot://chat/{}/http <= {}", model.name(), body);

                final var response = toChatResponse(body);
                consumer.accept(response);
                future.complete(response);
            } catch (Throwable ex) {
                onError(ex);
            }
        }

    }

    private static class StreamBodySubscriber implements HttpResponse.BodySubscriber<ChatResponse> {

        private final Charset charset;
        private final CompletableFuture<ChatResponse> future = new CompletableFuture<>();
        private final Collection<ChatResponse> responses = new ArrayList<>();
        private final ByteArrayOutputStream output = new ByteArrayOutputStream();
        private final byte[] bytes = new byte[10240];
        private final AtomicReference<Flow.Subscription> subscriptionRef = new AtomicReference<>();
        private final FeatureDetection detection = new FeatureDetection(new byte[]{'\n', '\n'});
        private final Model model;
        private final Consumer<ChatResponse> consumer;

        private StreamBodySubscriber(Model model, Charset charset, Consumer<ChatResponse> consumer) {
            this.model = model;
            this.charset = charset;
            this.consumer = consumer;
        }

        @Override
        public CompletionStage<ChatResponse> getBody() {
            return future;
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            if (subscriptionRef.compareAndSet(null, subscription)) {
                subscription.request(1);
            } else {
                subscription.cancel();
            }
        }

        private synchronized void flush() {
            synchronized (bytes) {
                try {
                    if (output.size() == 0) {
                        return;
                    }
                    final var body = output.toString(charset).trim();
                    logger.debug("erniebot://chat/{}/http <= {}", model.name(), body);
                    if (body.startsWith("data:")) {
                        final var segments = body.split(":", 2);
                        final var response = toChatResponse(segments[1]);
                        responses.add(response);
                        consumer.accept(response);
                    } else {
                        throw new RuntimeException("response format error: %s".formatted(body));
                    }
                } finally {
                    output.reset();
                }
            }
        }

        @Override
        public void onNext(List<ByteBuffer> buffers) {
            synchronized (bytes) {
                for (ByteBuffer buffer : buffers) {
                    while (buffer.hasRemaining()) {
                        final var length = Math.min(buffer.remaining(), bytes.length);
                        buffer.get(bytes, 0, length);
                        var offset = 0;
                        while (true) {
                            final var position = detection.screening(bytes, offset, length - offset);
                            if (position == -1) {
                                output.write(bytes, offset, length - offset);
                                break;
                            } else {
                                output.write(bytes, offset, position - offset);
                                offset = position + 1;
                                flush();
                            }
                        }
                    }
                }
            }
            subscriptionRef.get().request(1);
        }

        @Override
        public void onError(Throwable ex) {
            future.completeExceptionally(ex);
        }

        @Override
        public void onComplete() {
            try {

                // 如果管道中还有未结束的数据，则在这里处理
                flush();

                // 合并收到的ChatResponse
                final var response = responses.stream()
                        .reduce(this::merged)
                        .orElseThrow(() -> new RuntimeException("response is empty!"));

                future.complete(response);

            } catch (Throwable ex) {
                onError(ex);
            }
        }

        private ChatResponse merged(ChatResponse left, ChatResponse right) {
            return new ChatResponse(
                    left.id(),
                    left.type(),
                    left.timestamp(),
                    new ChatResponse.Sentence(
                            left.sentence().index(),
                            left.sentence().isLast() || right.sentence().isLast(),
                            left.sentence().content() + right.sentence().content()
                    ),
                    left.call(),
                    right.search(),
                    right.usage()
            );
        }

    }

}
