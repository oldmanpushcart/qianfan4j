package io.github.oldmanpushcart.internal.qianfan4j.embedding;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoResponseImpl;
import io.github.oldmanpushcart.qianfan4j.base.algo.Usage;
import io.github.oldmanpushcart.qianfan4j.base.api.Ret;
import io.github.oldmanpushcart.qianfan4j.embedding.Embedding;
import io.github.oldmanpushcart.qianfan4j.embedding.EmbeddingResponse;

import java.util.List;

public class EmbeddingResponseImpl extends AlgoResponseImpl implements EmbeddingResponse {

    private final List<Embedding> embeddings;

    private EmbeddingResponseImpl(String uuid, Ret ret, Usage usage, List<Embedding> embeddings) {
        super(uuid, ret, usage);
        this.embeddings = embeddings;
    }

    @Override
    public List<Embedding> embeddings() {
        return embeddings;
    }

    @JsonCreator
    static EmbeddingResponseImpl of(

            @JsonProperty("id")
            String uuid,

            @JsonProperty("error_code")
            String code,

            @JsonProperty("error_msg")
            String msg,

            @JsonProperty("usage")
            Usage usage,

            @JsonProperty("data")
            List<Embedding> embeddings

    ) {
        return new EmbeddingResponseImpl(uuid, Ret.of(code, msg), usage, embeddings);
    }

}
