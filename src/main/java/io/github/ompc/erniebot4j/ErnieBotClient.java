package io.github.ompc.erniebot4j;

import io.github.ompc.erniebot4j.chat.ChatRequest;
import io.github.ompc.erniebot4j.chat.ChatResponse;
import io.github.ompc.erniebot4j.chat.http.ChatExecutor;
import io.github.ompc.erniebot4j.completion.CompletionRequest;
import io.github.ompc.erniebot4j.completion.CompletionResponse;
import io.github.ompc.erniebot4j.completion.http.CompletionExecutor;
import io.github.ompc.erniebot4j.embedding.EmbeddingRequest;
import io.github.ompc.erniebot4j.embedding.EmbeddingResponse;
import io.github.ompc.erniebot4j.embedding.http.EmbeddingExecutor;
import io.github.ompc.erniebot4j.executor.Options;
import io.github.ompc.erniebot4j.image.caption.CaptionImageRequest;
import io.github.ompc.erniebot4j.image.caption.CaptionImageResponse;
import io.github.ompc.erniebot4j.image.caption.http.CaptionImageExecutor;
import io.github.ompc.erniebot4j.image.generation.GenImageRequest;
import io.github.ompc.erniebot4j.image.generation.GenImageResponse;
import io.github.ompc.erniebot4j.image.generation.http.GenImageExecutor;
import io.github.ompc.erniebot4j.plugin.PluginRequest;
import io.github.ompc.erniebot4j.plugin.PluginResponse;
import io.github.ompc.erniebot4j.plugin.http.PluginExecutor;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

/**
 * ErnieBot客户端
 */
public class ErnieBotClient {

    private final HttpClient http;
    private final TokenRefresher refresher;
    private final Executor executor;


    private ErnieBotClient(Builder builder) {
        this.http = newHttpClient(builder);
        this.refresher = requireNonNull(builder.refresher);
        this.executor = requireNonNull(builder.executor);
    }

    private HttpClient newHttpClient(Builder builder) {
        final var httpBuilder = HttpClient.newBuilder();
        ofNullable(builder.connectTimeout).ifPresent(httpBuilder::connectTimeout);
        ofNullable(builder.executor).ifPresent(httpBuilder::executor);
        return httpBuilder.build();
    }

    /**
     * 对话
     *
     * @param request 对话请求
     * @return 操作
     */
    public Op<ChatResponse> chat(ChatRequest request) {
        return consumer -> new ChatExecutor(http, refresher, executor)
                .execute(request, consumer);
    }

    /**
     * 补全
     *
     * @param request 补全请求
     * @return 操作
     */
    public Op<CompletionResponse> completion(CompletionRequest request) {
        return consumer -> new CompletionExecutor(http, refresher, executor)
                .execute(request, consumer);
    }

    /**
     * 图像
     *
     * @return 图像操作
     */
    public ImageOp image() {
        return new ImageOp() {

            @Override
            public Op<GenImageResponse> generation(GenImageRequest request) {
                return consumer -> new GenImageExecutor(http, refresher, executor)
                        .execute(request, consumer);
            }

            @Override
            public Op<CaptionImageResponse> caption(CaptionImageRequest request) {
                return consumer -> new CaptionImageExecutor(http, refresher, executor)
                        .execute(request, consumer);
            }

        };
    }

    /**
     * 向量计算
     *
     * @param request 向量计算请求
     * @return 操作
     */
    public Op<EmbeddingResponse> embedding(EmbeddingRequest request) {
        return consumer -> new EmbeddingExecutor(http, refresher, executor)
                .execute(request, consumer);
    }

    /**
     * 插件应用
     *
     * @param request 插件请求
     * @return 操作
     */
    public Op<PluginResponse> plugin(PluginRequest request) {
        return consumer -> new PluginExecutor(http, refresher, executor)
                .execute(request, consumer);
    }


    /**
     * 操作
     *
     * @param <R> 操作类型
     */
    public interface Op<R> {

        /**
         * 异步操作
         *
         * @return 操作结果
         */
        default CompletableFuture<R> async() {
            return stream(r -> {
            });
        }

        /**
         * 流式操作；流式操作有块、流两种模式，取决于请求选项中是否设置了{@link Options#IS_STREAM}
         * <ul>
         *     <li>块模式：将会消费最终应答</li>
         *     <li>流模式：将会消费每一个SSE产生的应答</li>
         * </ul>
         *
         * @param consumer 响应消费者
         * @return 操作结果
         */
        CompletableFuture<R> stream(Consumer<R> consumer);

    }

    /**
     * 图像操作
     */
    public interface ImageOp {

        /**
         * 文生图
         *
         * @param request 请求
         * @return 操作
         */
        Op<GenImageResponse> generation(GenImageRequest request);

        /**
         * 图生文
         *
         * @param request 请求
         * @return 操作
         */
        Op<CaptionImageResponse> caption(CaptionImageRequest request);

    }

    /**
     * 客户端构建器
     */
    public static class Builder {

        private TokenRefresher refresher;
        private Executor executor;
        private Duration connectTimeout;

        /**
         * 令牌刷新器
         *
         * @param refresher 令牌刷新器
         * @return this
         */
        public Builder refresher(TokenRefresher refresher) {
            this.refresher = refresher;
            return this;
        }

        /**
         * 线程池
         *
         * @param executor 线程池
         * @return this
         */
        public Builder executor(Executor executor) {
            this.executor = executor;
            return this;
        }

        /**
         * HTTP客户端连接超时
         *
         * @param connectTimeout 超时时间
         * @return this
         */
        public Builder connectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        /**
         * 构建客户端
         *
         * @return 客户端
         */
        public ErnieBotClient build() {
            return new ErnieBotClient(this);
        }

    }

}
