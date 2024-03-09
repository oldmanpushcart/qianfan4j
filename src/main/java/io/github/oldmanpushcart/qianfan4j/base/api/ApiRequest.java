package io.github.oldmanpushcart.qianfan4j.base.api;

import io.github.oldmanpushcart.qianfan4j.util.Buildable;

import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.function.Function;

/**
 * API请求
 */
public interface ApiRequest<R extends ApiResponse> {

    /**
     * 获取请求超时
     *
     * @return 请求超时
     */
    Duration timeout();

    /**
     * 获取选项
     *
     * @return 选项
     */
    Option option();

    /**
     * 转换为HTTP请求
     * @param token 请求令牌
     *
     * @return HTTP请求
     */
    HttpRequest newHttpRequest(String token);

    /**
     * 应答序列化
     *
     * @return 应答序列化
     */
    Function<String, R> responseDeserializer();

    /**
     * 构造器
     *
     * @param <T> 请求类型
     * @param <B> 构造器类型
     */
    interface Builder<T extends ApiRequest<?>, B extends Builder<T, B>> extends Buildable<T, B> {

        /**
         * 设置请求超时
         *
         * @param timeout 请求超时
         * @return this
         */
        B timeout(Duration timeout);

        /**
         * 设置选项
         *
         * @param opt   选项
         * @param value 选项值
         * @param <OT>  选项类型
         * @param <OR>  选项值类型
         * @return this
         */
        <OT, OR> B option(Option.Opt<OT, OR> opt, OT value);

        /**
         * 设置选项
         *
         * @param name  选项名
         * @param value 选项值
         * @return this
         */
        B option(String name, Object value);

    }

}
