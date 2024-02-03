package io.github.ompc.erniebot4j.chat.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.ompc.erniebot4j.TokenRefresher;
import io.github.ompc.erniebot4j.chat.ChatRequest;
import io.github.ompc.erniebot4j.chat.ChatResponse;
import io.github.ompc.erniebot4j.chat.message.Message;
import io.github.ompc.erniebot4j.executor.http.HttpExecutor;
import io.github.ompc.erniebot4j.util.JacksonUtils;
import io.github.ompc.erniebot4j.executor.http.TextualizableJsonSerializer;
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

public class ChatExecutor implements HttpExecutor<ChatRequest, ChatResponse> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final ObjectMapper mapper = JacksonUtils.mapper()
            .registerModule(new SimpleModule() {{
                addSerializer(ChatRequest.class, new ChatRequestJsonSerializer());
                addSerializer(Textualizable.class, new TextualizableJsonSerializer());
            }});
    private final TokenRefresher refresher;
    private final Executor executor;

    public ChatExecutor(TokenRefresher refresher, Executor executor) {
        this.refresher = refresher;
        this.executor = executor;
    }

    @Override
    public String toString() {
        return "erniebot://chat";
    }

    @Override
    public CompletableFuture<ChatResponse> execute(HttpClient http, ChatRequest request, Consumer<ChatResponse> consumer) {
        final var merged = new Merged();
        return execute(merged, http, request, consumer)
                .thenApply(response -> new ChatResponse(
                        response.id(),
                        response.type(),
                        response.timestamp(),
                        response.sentence(),
                        response.call(),
                        merged.search(),
                        merged.usage()
                ));
    }

    CompletableFuture<ChatResponse> execute(Merged merged, HttpClient http, ChatRequest request, Consumer<ChatResponse> consumer) {
        return refresher.refresh(http)

                // 获取token
                .thenCompose(token -> {

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

                    // 执行HTTP
                    return http.sendAsync(httpRequest, new ChatResponseBodyHandler(request.model(), consumer))
                            .thenApplyAsync(HttpResponse::body, executor)
                            .thenCompose(new ChatResponseHandler(merged, this, http, request, consumer));
                });

    }

}
