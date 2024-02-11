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

    private final List<String> documents;

    private EmbeddingRequest(Builder builder) {
        super(builder);
        this.documents = check(builder.documents, v -> null != v && !v.isEmpty(), "documents is empty");
    }

    /**
     * 获取文档
     *
     * @return 文档
     */
    public List<String> documents() {
        return documents;
    }

    /**
     * 向量计算请求构建器
     */
    public static class Builder extends BaseBuilder<EmbeddingModel, EmbeddingRequest, Builder> {

        private final List<String> documents = new ArrayList<>();

        /**
         * 设置文档
         *
         * @param documents 文档
         * @return this
         */
        public Builder documents(List<String> documents) {
            this.documents.addAll(documents);
            return this;
        }

        /**
         * @see #documents(List)
         */
        public Builder documents(String... documents) {
            return documents(List.of(documents));
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
