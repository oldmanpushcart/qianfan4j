package io.github.ompc.erniebot4j.executor.http;

import io.github.ompc.erniebot4j.executor.Request;
import io.github.ompc.erniebot4j.executor.Response;

import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * HTTP执行器
 *
 * @param <T> 请求类型
 * @param <R> 响应类型
 */
public interface HttpExecutor<T extends Request, R extends Response> {

    /**
     * 执行HTTP请求
     *
     * @param http     HTTP客户端
     * @param request  请求
     * @param consumer 响应消费者
     * @return 响应
     */
    CompletableFuture<R> execute(HttpClient http, T request, Consumer<R> consumer);

}
