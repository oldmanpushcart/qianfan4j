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
import io.github.ompc.erniebot4j.plugin.Plugin;
import io.github.ompc.erniebot4j.plugin.PluginRequest;
import io.github.ompc.erniebot4j.plugin.PluginResponse;
import io.github.ompc.erniebot4j.util.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class PluginExecutor implements HttpExecutor<PluginRequest, PluginResponse> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final ObjectMapper mapper = JacksonUtils.mapper()
            .registerModule(new SimpleModule() {{
                addDeserializer(PluginResponse.class, new PluginResponseJsonDeserializer());
            }});
    private final TokenRefresher refresher;
    private final Executor executor;
    private final HttpClient http;

    public PluginExecutor(TokenRefresher refresher, Executor executor, HttpClient http) {
        this.refresher = refresher;
        this.executor = executor;
        this.http = http;
    }

    @Override
    public CompletableFuture<PluginResponse> execute(PluginRequest request, Consumer<PluginResponse> consumer) {

        return serializePluginRequest(request)
                .thenCombine(refresher.refresh(http), (_json, _token) -> new Context() {{
                    this.httpRequestBodyJson = _json;
                    this.token = _token;
                }})
                .thenCompose(ctx -> {

                    // 记录HTTP请求日志
                    logger.debug("{}/{}/http => {}", this, request.model().name(), ctx.httpRequestBodyJson);

                    final var httpRequest = newHttpRequest(ctx, request);
                    final var responseBodyHandler = newResponseBodyHandler(ctx, request, consumer);

                    // 执行插件
                    return http.sendAsync(httpRequest, responseBodyHandler)
                            .thenApplyAsync(HttpResponse::body, executor);
                });

    }

    private CompletableFuture<String> serializePluginRequest(PluginRequest request) {
        return request.fetchImageUrl().<HashMap<Object, Object>>thenApply(imageUrl -> new HashMap<>() {{
                    put("query", request.question());

                    // 插件
                    if (!request.plugins().isEmpty()) {
                        put("plugins", request.plugins().stream()
                                .map(Plugin::text)
                                .toArray(String[]::new)
                        );
                    }

                    // 检查是否有图片
                    if (Objects.nonNull(imageUrl)) {
                        put("fileurl", imageUrl);
                    }

                    // 变量
                    if (!request.variables().isEmpty()) {
                        put("input_variables", request.variables());
                    }

                    // 对话历史
                    if (!request.messages().isEmpty()) {
                        put("history", request.messages());
                    }

                    // 选项参数
                    final var option = request.option().copy();
                    if (option.has("stream")) {
                        put("stream", option.remove("stream"));
                    }

                    // 是否返回RAW信息
                    if (option.has("verbose")) {
                        put("verbose", option.remove("verbose"));
                    }

                    // LLM参数
                    if (!option.isEmpty()) {
                        put("llm", option.export());
                    }

                }})
                .thenApply(map -> JacksonUtils.toJson(mapper, map));
    }

    // 构建HTTP请求
    private HttpRequest newHttpRequest(Context ctx, PluginRequest request) {
        final var builder = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create("%s?access_token=%s".formatted(request.model().remote(), ctx.token)))
                .POST(HttpRequest.BodyPublishers.ofString(ctx.httpRequestBodyJson));

        Optional.ofNullable(request.timeout()).ifPresent(builder::timeout);
        return builder.build();
    }

    // 构建请求处理器
    private ResponseBodyHandler<PluginResponse> newResponseBodyHandler(Context ctx, PluginRequest request, Consumer<PluginResponse> consumer) {
        return new ResponseBodyHandler.Builder<PluginResponse>()

                // 将json转为Response
                .convertor(json -> {

                    logger.debug("{}/{}/http <= {}", this, request.model().name(), json);

                    // 转为Node处理
                    final var node = JacksonUtils.toResponseNode(mapper, json);

                    // 检查是否为首包，plugin在SSE模式下开启了verbose的时候，首包为META-INFO
                    if (Objects.isNull(ctx.metaNodeRef.get()) && isMetaInfoNode(node)) {
                        ctx.metaNodeRef.set(node);
                        return null;
                    }

                    // 后续的包才是正式的应答
                    else {
                        final var responseNode = (ObjectNode) node;

                        // 恢复META-INFO
                        final var metaNode = ctx.metaNodeRef.get();
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

                    return JacksonUtils.toObject(mapper, PluginResponse.class, node);
                })

                // 消费Response
                .consumer(consumer)

                // 合并Response
                .accumulator(Aggregatable::accumulate)

                // 构造
                .build();
    }

    private boolean isMetaInfoNode(JsonNode node) {
        return node.has("plugin_id");
    }

    private static class Context {

        String httpRequestBodyJson;
        String token;
        final AtomicReference<JsonNode> metaNodeRef = new AtomicReference<>();

    }


    @Override
    public String toString() {
        return "erniebot://plugin";
    }

}
