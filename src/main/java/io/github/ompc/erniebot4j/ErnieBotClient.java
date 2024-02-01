package io.github.ompc.erniebot4j;

import io.github.ompc.erniebot4j.chat.ChatExecutor;
import io.github.ompc.erniebot4j.chat.ChatRequest;
import io.github.ompc.erniebot4j.chat.ChatResponse;

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

    public interface Op<R> {

        default CompletableFuture<R> async() {
            return stream(r -> {
            });
        }

        CompletableFuture<R> stream(Consumer<ChatResponse> consumer);

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
