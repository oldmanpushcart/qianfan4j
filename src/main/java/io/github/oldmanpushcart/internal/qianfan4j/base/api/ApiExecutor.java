package io.github.oldmanpushcart.internal.qianfan4j.base.api;

import io.github.oldmanpushcart.internal.qianfan4j.TokenRefresher;
import io.github.oldmanpushcart.qianfan4j.Constants;
import io.github.oldmanpushcart.qianfan4j.base.api.ApiException;
import io.github.oldmanpushcart.qianfan4j.base.api.ApiRequest;
import io.github.oldmanpushcart.qianfan4j.base.api.ApiResponse;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;

import static io.github.oldmanpushcart.internal.qianfan4j.base.api.http.HttpHeader.ContentType.MIME_APPLICATION_JSON;
import static io.github.oldmanpushcart.internal.qianfan4j.base.api.http.HttpHeader.HEADER_CONTENT_TYPE;
import static io.github.oldmanpushcart.internal.qianfan4j.base.api.http.HttpHeader.HEADER_X_QIANFAN_CLIENT;

public class ApiExecutor {

    private static final String CLIENT_INFO = "qianfan4j/%s".formatted(Constants.VERSION);
    private final TokenRefresher refresher;
    private final HttpClient http;
    private final Executor executor;

    public ApiExecutor(TokenRefresher refresher, HttpClient http, Executor executor) {
        this.refresher = refresher;
        this.http = http;
        this.executor = executor;
    }

    // 委派API请求
    private HttpRequest delegateHttpRequest(HttpRequest request, Consumer<HttpRequest.Builder> consumer) {
        final var builder = HttpRequest.newBuilder(request, (k, v) -> true);
        consumer.accept(builder);
        return builder.build();
    }

    public <R extends ApiResponse> CompletableFuture<R> execute(ApiRequest<R> request, BinaryOperator<R> accumulator, Consumer<R> consumer) {
        return refresher.refresh(http).thenCompose(token -> {
            final var httpRequest = delegateHttpRequest(request.newHttpRequest(token), builder -> builder
                    .headers(HEADER_X_QIANFAN_CLIENT, CLIENT_INFO)
                    .headers(HEADER_CONTENT_TYPE, MIME_APPLICATION_JSON));
            return http.sendAsync(httpRequest, new ApiResponseBodyHandler<>(request.responseDeserializer(), accumulator, consumer))
                    .thenApplyAsync(HttpResponse::body, executor)
                    .thenApply(response -> {

                        // 检查返回结果
                        if (!response.ret().isSuccess()) {
                            throw new ApiException(response);
                        }

                        return response;
                    });
        });
    }

}
