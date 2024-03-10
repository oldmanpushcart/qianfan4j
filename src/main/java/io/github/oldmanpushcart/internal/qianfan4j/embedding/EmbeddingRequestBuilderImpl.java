package io.github.oldmanpushcart.internal.qianfan4j.embedding;

import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoRequestBuilderImpl;
import io.github.oldmanpushcart.qianfan4j.embedding.EmbeddingModel;
import io.github.oldmanpushcart.qianfan4j.embedding.EmbeddingRequest;

import java.util.ArrayList;
import java.util.List;

public class EmbeddingRequestBuilderImpl extends AlgoRequestBuilderImpl<EmbeddingModel, EmbeddingRequest, EmbeddingRequest.Builder>
        implements EmbeddingRequest.Builder {

    private final List<String> texts = new ArrayList<>();

    @Override
    public EmbeddingRequest.Builder texts(List<String> texts) {
        this.texts.addAll(texts);
        return this;
    }

    @Override
    public EmbeddingRequest build() {
        return new EmbeddingRequestImpl(
                timeout(),
                model(),
                option(),
                user(),
                texts
        );
    }

}
