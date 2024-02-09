package io.github.ompc.erniebot4j.plugin.knowledgebase.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.ompc.erniebot4j.TokenRefresher;
import io.github.ompc.erniebot4j.exception.ErnieBotResponseNotSafeException;
import io.github.ompc.erniebot4j.executor.Aggregatable;
import io.github.ompc.erniebot4j.executor.http.HttpExecutor;
import io.github.ompc.erniebot4j.executor.http.ResponseBodyHandler;
import io.github.ompc.erniebot4j.plugin.knowledgebase.KnowledgeBaseRequest;
import io.github.ompc.erniebot4j.plugin.knowledgebase.KnowledgeBaseResponse;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class KnowledgeBaseExecutor implements HttpExecutor<KnowledgeBaseRequest, KnowledgeBaseResponse> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final ObjectMapper mapper = JacksonUtils.mapper()
            .registerModule(new SimpleModule() {{
                addSerializer(KnowledgeBaseRequest.class, new KnowledgeBaseRequestJsonSerializer());
                addDeserializer(KnowledgeBaseResponse.class, new KnowledgeBaseResponseJsonDeserializer());
                addDeserializer(KnowledgeBaseResponse.Meta.class, new KnowledgeBaseResponseMetaJsonDeserializer());
            }});
    private final TokenRefresher refresher;
    private final Executor executor;

    public KnowledgeBaseExecutor(TokenRefresher refresher, Executor executor) {
        this.refresher = refresher;
        this.executor = executor;
    }

    @Override
    public CompletableFuture<KnowledgeBaseResponse> execute(HttpClient http, KnowledgeBaseRequest request, Consumer<KnowledgeBaseResponse> consumer) {
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

            final var metaNodeRef = new AtomicReference<JsonNode>();

            // 构建请求处理器
            final var responseBodyHandler = new ResponseBodyHandler.Builder<KnowledgeBaseResponse>()

                    // 将json转为Response
                    .convertor(json -> {

                        logger.debug("{}/{}/http <= {}", this, request.model().name(), json);

                        // 转为Node处理
                        final var node = JacksonUtils.toResponseNode(mapper, json);

                        // 检查是否为首包，plugin在SSE模式下开启了verbose的时候，首包为META-INFO
                        if (!node.has("object")) {
                            metaNodeRef.set(node);
                            return null;
                        }

                        // 后续的包才是正式的应答
                        else {
                            final var responseNode = (ObjectNode) node;
                            responseNode.set("meta_info", metaNodeRef.get());
                        }

                        // 检查是否安全
                        if (node.has("need_clear_history") && node.get("need_clear_history").asBoolean()) {
                            throw new ErnieBotResponseNotSafeException(
                                    node.get("ban_round").asInt(),
                                    node.get("result").asText()
                            );
                        }

                        return JacksonUtils.toObject(mapper, KnowledgeBaseResponse.class, node);
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
        return "erniebot://plugin/kb";
    }

}
