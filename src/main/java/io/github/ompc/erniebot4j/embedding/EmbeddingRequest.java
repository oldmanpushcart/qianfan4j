package io.github.ompc.erniebot4j.embedding;

import io.github.ompc.erniebot4j.executor.BaseRequest;
import io.github.ompc.erniebot4j.executor.Request;

import java.util.ArrayList;
import java.util.List;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;

public class EmbeddingRequest extends BaseRequest<EmbeddingModel> implements Request {

    private final List<String> documents;

    private EmbeddingRequest(Builder builder) {
        super(builder);
        this.documents = check(builder.documents, v -> null != v && !v.isEmpty(), "documents is empty");
    }

    public List<String> documents() {
        return documents;
    }

    public static class Builder extends BaseBuilder<EmbeddingModel, EmbeddingRequest, Builder> {

        private final List<String> documents = new ArrayList<>();

        public Builder documents(List<String> documents) {
            this.documents.addAll(documents);
            return this;
        }

        public Builder documents(String... documents) {
            return documents(List.of(documents));
        }

        @Override
        public EmbeddingRequest build() {
            return new EmbeddingRequest(this);
        }

    }

}
