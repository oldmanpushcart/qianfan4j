package io.github.ompc.erniebot4j.image.caption.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.ompc.erniebot4j.TokenRefresher;
import io.github.ompc.erniebot4j.exception.ErnieBotResponseNotSafeException;
import io.github.ompc.erniebot4j.executor.Aggregatable;
import io.github.ompc.erniebot4j.executor.http.HttpExecutor;
import io.github.ompc.erniebot4j.executor.http.ResponseBodyHandler;
import io.github.ompc.erniebot4j.image.caption.CaptionImageRequest;
import io.github.ompc.erniebot4j.image.caption.CaptionImageResponse;
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

public class CaptionImageExecutor implements HttpExecutor<CaptionImageRequest, CaptionImageResponse> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final ObjectMapper mapper = JacksonUtils.mapper()
            .registerModule(new SimpleModule() {{
                addSerializer(CaptionImageRequest.class, new CaptionImageRequestJsonSerializer());
                addDeserializer(CaptionImageResponse.class, new CaptionImageResponseJsonDeserializer());
            }});

    private final TokenRefresher refresher;
    private final Executor executor;

    public CaptionImageExecutor(TokenRefresher refresher, Executor executor) {
        this.refresher = refresher;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<CaptionImageResponse> execute(HttpClient http, CaptionImageRequest request, Consumer<CaptionImageResponse> consumer) {
        return refresher.refresh(http).thenCompose(token -> {

            // 构建HTTP请求体
            final var httpRequestBodyJson = JacksonUtils.toJson(mapper, request);
            loggingHttpRequest(request, httpRequestBodyJson);

            // 构建HTTP请求
            final var builder = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .uri(URI.create("%s?access_token=%s".formatted(request.model().remote(), token)))
                    .POST(HttpRequest.BodyPublishers.ofString(httpRequestBodyJson));

            Optional.ofNullable(request.timeout()).ifPresent(builder::timeout);
            final var httpRequest = builder.build();

            // 构建请求处理器
            final var responseBodyHandler = new ResponseBodyHandler.Builder<CaptionImageResponse>()

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

                        return JacksonUtils.toObject(mapper, CaptionImageResponse.class, json);
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

    // 记录日志，这里需要做个特殊处理：图片的BASE64太大了导致日志输出刷屏，所以这里进行省略
    private void loggingHttpRequest(CaptionImageRequest request, String body) {
        if (logger.isDebugEnabled()) {
            String content;
            try {
                final var node = (ObjectNode) JacksonUtils.toNode(mapper, body);
                final var size = node.get("image").asText().length();
                node.put("image", "...(base64, size: %d bytes)".formatted(size));
                content = node.toString();
            } catch (Exception cause) {
                // ignore
                content = body;
            }
            logger.debug("{}/{}/http => {}", this, request.model().name(), content);
        }
    }

    @Override
    public String toString() {
        return "erniebot://image/caption";
    }

}
