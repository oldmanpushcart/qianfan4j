package io.github.ompc.erniebot4j.image.caption.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.ompc.erniebot4j.TokenRefresher;
import io.github.ompc.erniebot4j.executor.Sentence;
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

import static java.util.Objects.isNull;

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
            logger.debug("{}/{}/http => {}", this, request.model().name(), httpRequestBodyJson);

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
                            throw new RuntimeException("response is not safe!");
                        }

                        return JacksonUtils.toObject(mapper, CaptionImageResponse.class, json);
                    })

                    // 消费Response
                    .consumer(consumer)

                    // 合并Response
                    .accumulator((left, right) -> {
                        if (left == right || isNull(right)) {
                            return left;
                        } else if (isNull(left)) {
                            return right;
                        } else {
                            return new CaptionImageResponse(
                                    left.id(),
                                    left.type(),
                                    left.timestamp(),
                                    right.usage(),
                                    new Sentence(
                                            left.sentence().index(),
                                            left.sentence().isLast() || right.sentence().isLast(),
                                            left.sentence().content() + right.sentence().content()
                                    )
                            );
                        }
                    })

                    .build();

            return http.sendAsync(httpRequest, responseBodyHandler)
                    .thenApplyAsync(HttpResponse::body, executor);
        });
    }

    @Override
    public String toString() {
        return "erniebot://image/caption";
    }

}
