package io.github.ompc.erniebot4j.embedding.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.ompc.erniebot4j.TokenRefresher;
import io.github.ompc.erniebot4j.embedding.EmbeddingRequest;
import io.github.ompc.erniebot4j.embedding.EmbeddingResponse;
import io.github.ompc.erniebot4j.executor.http.HttpExecutor;
import io.github.ompc.erniebot4j.executor.http.TextualizableJsonSerializer;
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

public class EmbeddingExecutor implements HttpExecutor<EmbeddingRequest, EmbeddingResponse> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final ObjectMapper mapper = JacksonUtils.mapper()
            .registerModule(new SimpleModule() {{
                addSerializer(Textualizable.class, new TextualizableJsonSerializer());
                addSerializer(EmbeddingRequest.class, new EmbeddingRequestJsonSerializer());
                addDeserializer(EmbeddingResponse.class, new EmbeddingResponseJsonDeserializer());
            }});
    private final TokenRefresher refresher;
    private final Executor executor;

    public EmbeddingExecutor(TokenRefresher refresher, Executor executor) {
        this.refresher = refresher;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<EmbeddingResponse> execute(HttpClient http, EmbeddingRequest request, Consumer<EmbeddingResponse> consumer) {
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
                            .thenApply(body -> {
                                logger.debug("{}/{}/http <= {}", this, request.model().name(), body);
                                return JacksonUtils.toResponseNode(mapper, body);
                            })
                            .thenCompose(node -> {
                                final var response = JacksonUtils.toObject(mapper, EmbeddingResponse.class, node);
                                consumer.accept(response);
                                return CompletableFuture.completedFuture(response);
                            });

                });
    }

    @Override
    public String toString() {
        return "erniebot://embedding";
    }

}