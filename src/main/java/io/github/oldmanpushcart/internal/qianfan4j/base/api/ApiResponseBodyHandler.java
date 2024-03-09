package io.github.oldmanpushcart.internal.qianfan4j.base.api;

import io.github.oldmanpushcart.internal.qianfan4j.base.api.http.HttpHeader;
import io.github.oldmanpushcart.internal.qianfan4j.util.FeatureDetection;
import io.github.oldmanpushcart.qianfan4j.base.api.ApiResponse;

import java.io.ByteArrayOutputStream;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.github.oldmanpushcart.internal.qianfan4j.base.api.http.HttpHeader.ContentType.MIME_APPLICATION_JSON;
import static io.github.oldmanpushcart.internal.qianfan4j.base.api.http.HttpHeader.ContentType.MIME_TEXT_EVENT_STREAM;

public class ApiResponseBodyHandler<R extends ApiResponse> implements HttpResponse.BodyHandler<R> {

    private final Function<String, R> deserializer;
    private final BinaryOperator<R> accumulator;
    private final Consumer<R> consumer;

    /**
     * 构造响应处理器
     *
     * @param deserializer 应答反序列化器
     * @param accumulator  应答累加器
     * @param consumer     应答消费者
     */
    public ApiResponseBodyHandler(Function<String, R> deserializer, BinaryOperator<R> accumulator, Consumer<R> consumer) {
        this.deserializer = deserializer;
        this.accumulator = accumulator;
        this.consumer = consumer;
    }

    @Override
    public HttpResponse.BodySubscriber<R> apply(HttpResponse.ResponseInfo info) {
        final var ct = HttpHeader.ContentType.parse(info.headers());
        final var charset = ct.charset();
        return switch (ct.mime()) {
            case MIME_APPLICATION_JSON -> new BlockBodySubscriber(charset);
            case MIME_TEXT_EVENT_STREAM -> new StreamBodySubscriber(charset);
            default -> throw new IllegalStateException("illegal http Content-Type: %s".formatted(ct.mime()));
        };
    }


    /**
     * 块响应订阅器
     */
    private class BlockBodySubscriber implements HttpResponse.BodySubscriber<R> {

        private final CompletableFuture<R> future = new CompletableFuture<>();
        private final ByteArrayOutputStream output = new ByteArrayOutputStream();
        private final byte[] bytes = new byte[10240];
        private final Charset charset;

        private BlockBodySubscriber(Charset charset) {
            this.charset = charset;
        }

        @Override
        public CompletionStage<R> getBody() {
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
                final var response = deserializer.apply(body);
                consumer.accept(response);
                future.complete(response);
            } catch (Throwable ex) {
                onError(ex);
            }
        }

    }

    /**
     * 流响应订阅器
     */
    private class StreamBodySubscriber implements HttpResponse.BodySubscriber<R> {

        private final CompletableFuture<R> future = new CompletableFuture<>();
        private final AtomicReference<R> responseRef = new AtomicReference<>();
        private final ByteArrayOutputStream output = new ByteArrayOutputStream();
        private final byte[] bytes = new byte[10240];
        private final AtomicReference<Flow.Subscription> subscriptionRef = new AtomicReference<>();
        private final FeatureDetection detection = new FeatureDetection(new byte[]{'\n', '\n'});
        private final Charset charset;

        private StreamBodySubscriber(Charset charset) {
            this.charset = charset;
        }

        @Override
        public CompletionStage<R> getBody() {
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
                    if (body.startsWith("data:")) {
                        final var segments = body.split(":", 2);
                        final var response = deserializer.apply(segments[1].trim());
                        if (null != response) {
                            responseRef.accumulateAndGet(response, accumulator);
                        }
                    } else {
                        throw new RuntimeException("unsupported stream-event: %s".formatted(body));
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
                final var response = Optional.ofNullable(responseRef.get())
                        .orElseThrow(() -> new RuntimeException("response is empty!"));

                future.complete(response);

            } catch (Throwable ex) {
                onError(ex);
            }

        }

    }

}
