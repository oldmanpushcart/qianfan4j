package io.github.ompc.erniebot4j.embedding;

import io.github.ompc.erniebot4j.executor.BaseRequest;
import io.github.ompc.erniebot4j.executor.Request;

import java.util.ArrayList;
import java.util.List;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;

/**
 * 向量计算请求
 */
public final class EmbeddingRequest extends BaseRequest<EmbeddingModel> implements Request {

    private final List<String> texts;

    private EmbeddingRequest(Builder builder) {
        super(builder);
        this.texts = check(builder.texts, v -> null != v && !v.isEmpty(), "texts is empty");
    }

    /**
     * 获取文本
     *
     * @return 文本
     */
    public List<String> texts() {
        return texts;
    }

    /**
     * 向量计算请求构建器
     */
    public static class Builder extends BaseBuilder<EmbeddingModel, EmbeddingRequest, Builder> {

        private final List<String> texts = new ArrayList<>();

        /**
         * 设置文本
         *
         * @param texts 文本
         * @return this
         */
        public Builder texts(List<String> texts) {
            this.texts.addAll(texts);
            return this;
        }

        /**
         * @see #texts(List)
         */
        public Builder texts(String... texts) {
            return texts(List.of(texts));
        }

        /**
         * 构建Embedding请求
         *
         * @return Embedding请求
         */
        @Override
        public EmbeddingRequest build() {
            return new EmbeddingRequest(this);
        }

    }

}
