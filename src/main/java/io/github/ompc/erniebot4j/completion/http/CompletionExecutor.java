package io.github.ompc.erniebot4j.completion.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.ompc.erniebot4j.TokenRefresher;
import io.github.ompc.erniebot4j.completion.CompletionRequest;
import io.github.ompc.erniebot4j.completion.CompletionResponse;
import io.github.ompc.erniebot4j.exception.ErnieBotResponseNotSafeException;
import io.github.ompc.erniebot4j.executor.Aggregatable;
import io.github.ompc.erniebot4j.executor.http.HttpExecutor;
import io.github.ompc.erniebot4j.executor.http.ResponseBodyHandler;
import io.github.ompc.erniebot4j.util.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * 续写请求执行器
 */
public class CompletionExecutor implements HttpExecutor<CompletionRequest, CompletionResponse> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final ObjectMapper mapper = JacksonUtils.mapper()
            .registerModule(new SimpleModule() {{
                addSerializer(CompletionRequest.class, new CompletionRequestJsonSerializer());
                addDeserializer(CompletionResponse.class, new CompletionResponseJsonDeserializer());
            }});

    private final TokenRefresher refresher;
    private final Executor executor;
    private final HttpClient http;

    public CompletionExecutor(HttpClient http, TokenRefresher refresher, Executor executor) {
        this.refresher = refresher;
        this.executor = executor;
        this.http = http;
    }

    @Override
    public CompletableFuture<CompletionResponse> execute(CompletionRequest request, Consumer<CompletionResponse> consumer) {
        return refresher.refresh(http).thenCompose(token -> {

            // 构建HTTP请求体
            final var httpRequestBodyJson = JacksonUtils.toJson(mapper, request);
            logger.debug("{}/{}/http => {}", this, request.model().name(), httpRequestBodyJson);

            // 构建HTTP请求
            final var builder = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .uri(URI.create("%s?access_token=%s".formatted(request.model().remote(), token)))
                    .POST(HttpRequest.BodyPublishers.ofString(httpRequestBodyJson));

            Optional.ofNullable(request.timeout()).ifPresent(builder::timeout);
            final var httpRequest = builder.build();

            // 构建请求处理器
            final var responseBodyHandler = new ResponseBodyHandler.Builder<CompletionResponse>()

                    // 将json转为Response
                    .convertor(json -> {

                        logger.debug("{}/{}/http <= {}", this, request.model().name(), json);

                        // 转为Node处理
                        final var node = JacksonUtils.toResponseNode(mapper, json);

                        // 检查是否安全
                        if (node.has("is_safe") && node.get("is_safe").asInt() == 0) {
                            throw new ErnieBotResponseNotSafeException(
                                    node.get("result").asText()
                            );
                        }

                        return JacksonUtils.toObject(mapper, CompletionResponse.class, node);
                    })

                    // 消费Response
                    .consumer(consumer)

                    // 合并Response
                    .accumulator(Aggregatable::accumulate)

                    .build();

            return http.sendAsync(httpRequest, responseBodyHandler)
                    .thenApplyAsync(HttpResponse::body, executor);
        });
    }

    @Override
    public String toString() {
        return "erniebot://completion";
    }

}
