package io.github.ompc.erniebot4j.executor.http;

import io.github.ompc.erniebot4j.util.FeatureDetection;

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

import static io.github.ompc.erniebot4j.executor.http.HttpContentType.MIME_APPLICATION_JSON;
import static io.github.ompc.erniebot4j.executor.http.HttpContentType.MIME_TEXT_EVENT_STREAM;
import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElseGet;

public class ResponseBodyHandler<R> implements HttpResponse.BodyHandler<R> {

    private final Function<String, R> convertor;
    private final Consumer<R> consumer;
    private final BinaryOperator<R> accumulator;

    public ResponseBodyHandler(Builder<R> builder) {
        this.convertor = requireNonNull(builder.convertor);
        this.consumer = requireNonNullElseGet(builder.consumer, () -> r -> {

        });
        this.accumulator = requireNonNullElseGet(builder.accumulator, () -> (left, right) -> right);
    }

    public static class Builder<R> {
        private Function<String, R> convertor;
        private Consumer<R> consumer;
        private BinaryOperator<R> accumulator;

        public Builder<R> convertor(Function<String, R> convertor) {
            this.convertor = convertor;
            return this;
        }

        public Builder<R> consumer(Consumer<R> consumer) {
            this.consumer = consumer;
            return this;
        }

        public Builder<R> accumulator(BinaryOperator<R> accumulator) {
            this.accumulator = accumulator;
            return this;
        }

        public ResponseBodyHandler<R> build() {
            return new ResponseBodyHandler<>(this);
        }
    }

    @Override
    public HttpResponse.BodySubscriber<R> apply(HttpResponse.ResponseInfo info) {
        final var ct = HttpContentType.parse(info.headers());
        final var charset = ct.charset();
        return switch (ct.mime()) {
            case MIME_APPLICATION_JSON -> new BlockBodySubscriber(charset);
            case MIME_TEXT_EVENT_STREAM -> new StreamBodySubscriber(charset);
            default -> throw new RuntimeException("unsupported http Content-Type: %s".formatted(ct.mime()));
        };
    }

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
                final var response = convertor.apply(body);
                consumer.accept(response);
                future.complete(response);
            } catch (Throwable ex) {
                onError(ex);
            }
        }

    }

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
                        final var response = convertor.apply(segments[1].trim());
                        consumer.accept(response);
                        responseRef.accumulateAndGet(response, accumulator);
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
