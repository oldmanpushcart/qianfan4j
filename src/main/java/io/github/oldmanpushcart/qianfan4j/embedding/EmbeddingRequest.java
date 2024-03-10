package io.github.oldmanpushcart.qianfan4j.embedding;

import io.github.oldmanpushcart.internal.qianfan4j.embedding.EmbeddingRequestBuilderImpl;
import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoRequest;

import java.util.List;

public interface EmbeddingRequest extends AlgoRequest<EmbeddingModel, EmbeddingResponse> {

    /**
     * 获取文本集合
     *
     * @return 文本集合
     */
    List<String> texts();

    /**
     * 创建构建器
     *
     * @return 构建器
     */
    static Builder newBuilder() {
        return new EmbeddingRequestBuilderImpl();
    }

    /**
     * 构建器
     */
    interface Builder extends AlgoRequest.Builder<EmbeddingModel, EmbeddingRequest, EmbeddingRequest.Builder> {

        /**
         * 设置文本集合
         *
         * @param texts 文本集合
         * @return this
         */
        default Builder texts(String... texts) {
            return texts(List.of(texts));
        }

        /**
         * 设置文本集合
         *
         * @param texts 文本集合
         * @return this
         */
        Builder texts(List<String> texts);

    }

}
