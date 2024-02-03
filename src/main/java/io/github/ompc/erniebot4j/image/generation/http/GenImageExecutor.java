package io.github.ompc.erniebot4j.image.generation.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.ompc.erniebot4j.TokenRefresher;
import io.github.ompc.erniebot4j.chat.message.Message;
import io.github.ompc.erniebot4j.executor.http.HttpExecutor;
import io.github.ompc.erniebot4j.executor.http.TextualizableJsonSerializer;
import io.github.ompc.erniebot4j.image.generation.GenImageRequest;
import io.github.ompc.erniebot4j.image.generation.GenImageResponse;
import io.github.ompc.erniebot4j.util.JacksonUtils;
import io.github.ompc.erniebot4j.util.Textualizable;
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

public class GenImageExecutor implements HttpExecutor<GenImageRequest, GenImageResponse> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final ObjectMapper mapper = JacksonUtils.mapper()
            .registerModule(new SimpleModule() {{
                addSerializer(GenImageRequest.class, new GenImageRequestJsonSerializer());
                addSerializer(Textualizable.class, new TextualizableJsonSerializer());
                addDeserializer(GenImageResponse.class, new GenImageResponseJsonDeserializer());
            }});

    private final TokenRefresher refresher;
    private final Executor executor;

    public GenImageExecutor(TokenRefresher refresher, Executor executor) {
        this.refresher = refresher;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<GenImageResponse> execute(HttpClient http, GenImageRequest request, Consumer<GenImageResponse> consumer) {
        return refresher.refresh(http)
                .thenCompose(token -> {

                    final var httpRequestBodyJson = JacksonUtils.toJson(mapper, request);
                    logger.debug("{}/{}/http => {}", this, request.model().name(), httpRequestBodyJson);

                    // 构建HTTP请求
                    final var builder = HttpRequest.newBuilder()
                            .header("Content-Type", "application/json")
                            .uri(URI.create("%s?access_token=%s".formatted(request.model().remote(), token)))
                            .POST(HttpRequest.BodyPublishers.ofString(httpRequestBodyJson));

                    Optional.ofNullable(request.timeout()).ifPresent(builder::timeout);
                    final var httpRequest = builder.build();

                    return http.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                            .thenApplyAsync(HttpResponse::body, executor)
                            .thenApply(body-> {
                                logger.debug("{}/{}/http <= {}", this, request.model().name(), body);
                                final var node = JacksonUtils.toNode(mapper, body);

                                // 处理返回错误
                                if (node.has("error_code")) {
                                    throw new RuntimeException("response error: %s; %s".formatted(
                                            node.get("error_code").asInt(),
                                            node.has("error_msg")
                                                    ? node.get("error_msg").asText()
                                                    : null
                                    ));
                                }

                                return node;
                            })
                            .thenCompose(node -> {
                                final var response = JacksonUtils.toObject(mapper, GenImageResponse.class, node);
                                return CompletableFuture.completedFuture(response);
                            });
                });
    }

    @Override
    public String toString() {
        return "erniebot://image/generation";
    }

}
