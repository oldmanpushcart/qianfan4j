package io.github.oldmanpushcart.internal.qianfan4j;

import io.github.oldmanpushcart.internal.qianfan4j.base.api.ApiExecutor;
import io.github.oldmanpushcart.internal.qianfan4j.chat.ChatResponseHandler;
import io.github.oldmanpushcart.qianfan4j.QianFanClient;
import io.github.oldmanpushcart.qianfan4j.chat.ChatRequest;
import io.github.oldmanpushcart.qianfan4j.chat.ChatResponse;
import io.github.oldmanpushcart.qianfan4j.util.Aggregatable;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executor;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public class QianFanClientImpl implements QianFanClient {

    private final ApiExecutor apiExecutor;

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
        return consumer -> apiExecutor.execute(request, Aggregatable::aggregate, consumer)
                .thenCompose(new ChatResponseHandler(apiExecutor, request, consumer));
    }


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
