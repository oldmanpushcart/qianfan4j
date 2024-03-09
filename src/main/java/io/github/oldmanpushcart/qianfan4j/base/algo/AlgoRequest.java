package io.github.oldmanpushcart.qianfan4j.base.algo;

import io.github.oldmanpushcart.qianfan4j.base.api.ApiRequest;

import java.util.function.Consumer;

public interface AlgoRequest<M extends Model, R extends AlgoResponse> extends ApiRequest<R> {

    /**
     * 获取模型
     *
     * @return 模型
     */
    M model();

    /**
     * 请求用户标识；用于跟踪和调试请求
     *
     * @return 请求用户标识
     */
    String user();

    interface Builder<M extends Model, T extends AlgoRequest<M, ?>, B extends Builder<M, T, B>> extends ApiRequest.Builder<T, B> {

        /**
         * 设置模型
         *
         * @param model 模型
         * @return this
         */
        B model(M model);

        /**
         * 设置请求用户标识
         *
         * @param user 请求用户标识
         * @return this
         */
        B user(String user);

        /**
         * 构建
         *
         * @param building 构建函数
         * @return this
         */
        default B building(Consumer<B> building) {
            building.accept(self());
            return self();
        }

    }

}
