package io.github.oldmanpushcart.internal.qianfan4j;

import io.github.oldmanpushcart.internal.qianfan4j.base.api.ApiExecutor;
import io.github.oldmanpushcart.internal.qianfan4j.chat.ChatResponseHandler;
import io.github.oldmanpushcart.qianfan4j.QianFanClient;
import io.github.oldmanpushcart.qianfan4j.chat.ChatRequest;
import io.github.oldmanpushcart.qianfan4j.chat.ChatResponse;
import io.github.oldmanpushcart.qianfan4j.completion.CompletionRequest;
import io.github.oldmanpushcart.qianfan4j.completion.CompletionResponse;
import io.github.oldmanpushcart.qianfan4j.embedding.EmbeddingRequest;
import io.github.oldmanpushcart.qianfan4j.embedding.EmbeddingResponse;
import io.github.oldmanpushcart.qianfan4j.image.caption.CaptionImageRequest;
import io.github.oldmanpushcart.qianfan4j.image.caption.CaptionImageResponse;
import io.github.oldmanpushcart.qianfan4j.image.generation.GenerationImageRequest;
import io.github.oldmanpushcart.qianfan4j.image.generation.GenerationImageResponse;
import io.github.oldmanpushcart.qianfan4j.util.Aggregator;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executor;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * 千帆客户端实现
 */
public class QianFanClientImpl implements QianFanClient {

    private final ApiExecutor apiExecutor;

    /**
     * 构造千帆客户端实现
     *
     * @param builder 构造器
     */
    public QianFanClientImpl(Builder builder) {
        this.apiExecutor = new ApiExecutor(
                new TokenRefresher(builder.ak, builder.sk),
                newHttpClient(builder),
                requireNonNull(builder.executor)
        );
    }

    // 构建HTTP客户端
    private HttpClient newHttpClient(Builder builder) {
        final var httpBuilder = HttpClient.newBuilder();
        ofNullable(builder.connectTimeout).ifPresent(httpBuilder::connectTimeout);
        ofNullable(builder.executor).ifPresent(httpBuilder::executor);
        return httpBuilder.build();
    }

    @Override
    public Op<ChatResponse> chat(ChatRequest request) {
        return consumer -> apiExecutor.execute(request, Aggregator::accumulate, consumer)
                .thenCompose(new ChatResponseHandler(this, request, consumer));
    }

    @Override
    public Op<CompletionResponse> completion(CompletionRequest request) {
        return consumer -> apiExecutor.execute(request, Aggregator::accumulate, consumer);
    }

    @Override
    public Op<CaptionImageResponse> captionImage(CaptionImageRequest request) {
        return consumer -> apiExecutor.execute(request, Aggregator::accumulate, consumer);
    }

    @Override
    public Op<GenerationImageResponse> generationImage(GenerationImageRequest request) {
        return consumer -> apiExecutor.execute(request, (r1, r2) -> r2, consumer);
    }

    @Override
    public Op<EmbeddingResponse> embedding(EmbeddingRequest request) {
        return consumer -> apiExecutor.execute(request, (r1, r2) -> r2, consumer);
    }

    /**
     * 千帆客户端构造器实现
     */
    public static class Builder implements QianFanClient.Builder {

        private String ak;
        private String sk;
        private Executor executor;
        private Duration connectTimeout;

        @Override
        public Builder ak(String ak) {
            this.ak = ak;
            return this;
        }

        @Override
        public Builder sk(String sk) {
            this.sk = sk;
            return this;
        }

        @Override
        public Builder executor(Executor executor) {
            this.executor = executor;
            return this;
        }

        @Override
        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        @Override
        public QianFanClient build() {
            return new QianFanClientImpl(this);
        }

    }

}
