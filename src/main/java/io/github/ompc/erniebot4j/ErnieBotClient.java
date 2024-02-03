package io.github.ompc.erniebot4j;

import io.github.ompc.erniebot4j.chat.ChatRequest;
import io.github.ompc.erniebot4j.chat.ChatResponse;
import io.github.ompc.erniebot4j.chat.http.ChatExecutor;
import io.github.ompc.erniebot4j.embedding.EmbeddingRequest;
import io.github.ompc.erniebot4j.embedding.EmbeddingResponse;
import io.github.ompc.erniebot4j.embedding.http.EmbeddingExecutor;
import io.github.ompc.erniebot4j.image.caption.CaptionImageRequest;
import io.github.ompc.erniebot4j.image.caption.CaptionImageResponse;
import io.github.ompc.erniebot4j.image.caption.http.CaptionImageExecutor;
import io.github.ompc.erniebot4j.image.generation.GenImageRequest;
import io.github.ompc.erniebot4j.image.generation.GenImageResponse;
import io.github.ompc.erniebot4j.image.generation.http.GenImageExecutor;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class ErnieBotClient {

    private final TokenRefresher refresher;
    private final Executor executor;
    private final HttpClient http;

    public ErnieBotClient(Builder builder) {
        this.refresher = requireNonNull(builder.refresher);
        this.executor = requireNonNull(builder.executor);

        final var httpBuilder = HttpClient.newBuilder().executor(executor);
        Optional.ofNullable(builder.connectTimeout).ifPresent(httpBuilder::connectTimeout);
        this.http = httpBuilder.build();
    }

    public Op<ChatResponse> chat(ChatRequest request) {
        return consumer -> new ChatExecutor(refresher, executor)
                .execute(http, request, consumer);
    }

    public ImageOp image() {
        return new ImageOp() {

            @Override
            public Op<GenImageResponse> generation(GenImageRequest request) {
                return consumer -> new GenImageExecutor(refresher, executor)
                        .execute(http, request, consumer);
            }

            @Override
            public Op<CaptionImageResponse> caption(CaptionImageRequest request) {
                return consumer -> new CaptionImageExecutor(refresher, executor)
                        .execute(http, request, consumer);
            }

        };
    }

    public Op<EmbeddingResponse> embedding(EmbeddingRequest request) {
        return consumer -> new EmbeddingExecutor(refresher, executor)
                .execute(http, request, consumer);
    }

    public interface Op<R> {

        default CompletableFuture<R> async() {
            return stream(r -> {
            });
        }

        CompletableFuture<R> stream(Consumer<R> consumer);

    }

    public interface ImageOp {

        Op<GenImageResponse> generation(GenImageRequest request);

        Op<CaptionImageResponse> caption(CaptionImageRequest request);

    }

    public static class Builder {

        private TokenRefresher refresher;
        private Executor executor;
        private Duration connectTimeout;

        public Builder refresher(TokenRefresher refresher) {
            this.refresher = refresher;
            return this;
        }

        public Builder executor(Executor executor) {
            this.executor = executor;
            return this;
        }

        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public ErnieBotClient build() {
            return new ErnieBotClient(this);
        }

    }

}
