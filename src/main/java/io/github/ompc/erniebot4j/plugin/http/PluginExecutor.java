package io.github.ompc.erniebot4j.plugin.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.ompc.erniebot4j.TokenRefresher;
import io.github.ompc.erniebot4j.exception.ErnieBotResponseNotSafeException;
import io.github.ompc.erniebot4j.executor.Aggregatable;
import io.github.ompc.erniebot4j.executor.http.HttpExecutor;
import io.github.ompc.erniebot4j.executor.http.ResponseBodyHandler;
import io.github.ompc.erniebot4j.plugin.PluginRequest;
import io.github.ompc.erniebot4j.plugin.PluginResponse;
import io.github.ompc.erniebot4j.util.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 插件执行器
 */
public class PluginExecutor implements HttpExecutor<PluginRequest, PluginResponse> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final ObjectMapper mapper = JacksonUtils.mapper()
            .registerModule(new SimpleModule() {{
                addSerializer(PluginRequest.class, new PluginRequestJsonSerializer());
                addDeserializer(PluginResponse.class, new PluginResponseJsonDeserializer());
            }});
    private final TokenRefresher refresher;
    private final Executor executor;
    private final HttpClient http;

    public PluginExecutor(HttpClient http, TokenRefresher refresher, Executor executor) {
        this.refresher = refresher;
        this.executor = executor;
        this.http = http;
    }

    @Override
    public CompletableFuture<PluginResponse> execute(PluginRequest request, Consumer<PluginResponse> consumer) {

        return refresher.refresh(http).thenCompose(token -> {

            // HTTP请求
            final var httpRequest = newHttpRequest(token, request);

            // HTTP应答处理
            final var httpResponseBodyHandler = newHttpResponseBodyHandler(request, consumer);

            // 执行插件
            return http.sendAsync(httpRequest, httpResponseBodyHandler)
                    .thenApplyAsync(HttpResponse::body, executor);

        });

    }

    // 构造HTTP请求
    private HttpRequest newHttpRequest(String token, PluginRequest request) {
        // 构建HTTP请求体
        final var httpRequestBodyJson = JacksonUtils.toJson(mapper, request);

        // 记录HTTP请求日志
        logger.debug("{}/{}/http => {}", this, request.model().name(), httpRequestBodyJson);

        // 构建HTTP请求
        final var builder = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create("%s?access_token=%s".formatted(request.model().remote(), token)))
                .POST(HttpRequest.BodyPublishers.ofString(httpRequestBodyJson));

        Optional.ofNullable(request.timeout()).ifPresent(builder::timeout);
        return builder.build();
    }

    // 构造HTTP应答处理器
    private HttpResponse.BodyHandler<PluginResponse> newHttpResponseBodyHandler(PluginRequest request, Consumer<PluginResponse> consumer) {
        final var metaNodeRef = new AtomicReference<JsonNode>();
        return new ResponseBodyHandler.Builder<PluginResponse>()

                // 将json转为Response
                .convertor(json -> {

                    // 记录HTTP请求日志
                    logger.debug("{}/{}/http <= {}", this, request.model().name(), json);

                    // 转为Node处理
                    final var node = JacksonUtils.toResponseNode(mapper, json);

                    // 检查是否为SSE首包，plugin在SSE模式下开启了verbose的时候，首包为META-INFO
                    if (isFirstSSE(metaNodeRef, node)) {
                        metaNodeRef.set(node);
                        return null;
                    }

                    // 后续的包才是正式的应答
                    else {
                        final var responseNode = (ObjectNode) node;

                        // 恢复META-INFO
                        final var metaNode = metaNodeRef.get();
                        if (Objects.nonNull(metaNode)) {
                            responseNode.set("meta_info", metaNode);
                        }

                    }

                    // 检查是否安全
                    if (node.has("need_clear_history") && node.get("need_clear_history").asBoolean()) {
                        throw new ErnieBotResponseNotSafeException(
                                node.get("ban_round").asInt(),
                                node.get("result").asText()
                        );
                    }

                    // 反序列化应答
                    return JacksonUtils.toObject(mapper, PluginResponse.class, node);
                })

                // 消费Response
                .consumer(consumer)

                // 合并Response
                .accumulator(Aggregatable::accumulate)

                // 构造
                .build();

    }


    // 是否SSE的首包
    private boolean isFirstSSE(AtomicReference<?> ref, JsonNode node) {
        return Objects.isNull(ref.get())
                && node.has("plugin_id");
    }

    @Override
    public String toString() {
        return "erniebot://plugin";
    }

}
