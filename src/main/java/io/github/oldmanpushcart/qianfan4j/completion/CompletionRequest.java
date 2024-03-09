package io.github.oldmanpushcart.qianfan4j.completion;

import io.github.oldmanpushcart.internal.qianfan4j.completion.CompletionRequestBuilderImpl;
import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoRequest;

/**
 * 续写请求
 */
public interface CompletionRequest extends AlgoRequest<CompletionModel, CompletionResponse> {

    /**
     * 获取续写提示
     *
     * @return 续写提示
     */
    String prompt();

    /**
     * 创建续写请求构造器
     *
     * @return 构造器
     */
    static Builder newBuilder() {
        return new CompletionRequestBuilderImpl();
    }

    /**
     * 续写请求构造器
     */
    interface Builder extends AlgoRequest.Builder<CompletionModel, CompletionRequest, Builder> {

        /**
         * 设置续写提示
         *
         * @param prompt 续写提示
         * @return this
         */
        Builder prompt(String prompt);

    }

}
