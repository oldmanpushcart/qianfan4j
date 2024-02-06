package io.github.ompc.erniebot4j.chat.function;

import java.util.concurrent.CompletableFuture;

/**
 * 函数
 *
 * @param <T> 参数类型
 * @param <R> 返回值类型
 */
@FunctionalInterface
public interface ChatFunction<T, R> {

    /**
     * 函数调用
     *
     * @param t 参数
     * @return 返回值
     */
    CompletableFuture<R> call(T t);

}
