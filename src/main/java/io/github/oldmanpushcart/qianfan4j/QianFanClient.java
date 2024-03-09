package io.github.oldmanpushcart.qianfan4j;

import io.github.oldmanpushcart.internal.qianfan4j.QianFanClientImpl;
import io.github.oldmanpushcart.qianfan4j.base.api.ApiOptions;
import io.github.oldmanpushcart.qianfan4j.chat.ChatRequest;
import io.github.oldmanpushcart.qianfan4j.chat.ChatResponse;
import io.github.oldmanpushcart.qianfan4j.util.Buildable;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * 千帆客户端
 */
public interface QianFanClient {

    /**
     * 对话
     *
     * @param request 对话请求
     * @return 对话操作
     */
    Op<ChatResponse> chat(ChatRequest request);

    /**
     * 千帆客户端构造器
     *
     * @return 构造器
     */
    static Builder newBuilder() {
        return new QianFanClientImpl.Builder();
    }

    /**
     * 操作
     *
     * @param <R> 操作类型
     */
    interface Op<R> {

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
         * 流式操作；流式操作有块、流两种模式，取决于请求选项中是否设置了{@link ApiOptions#IS_STREAM}
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
     * 客户端构建器
     */
    interface Builder extends Buildable<QianFanClient, Builder> {

        /**
         * 设置AK
         *
         * @param ak AK
         * @return this
         */
        Builder ak(String ak);

        /**
         * 设置SK
         *
         * @param sk SK
         * @return this
         */
        Builder sk(String sk);

        /**
         * 设置线程池
         *
         * @param executor 线程池
         * @return this
         */
        Builder executor(Executor executor);

        /**
         * 设置连接超时
         *
         * @param connectTimeout 连接超时
         * @return this
         */
        Builder connectTimeout(Duration connectTimeout);

    }

}
