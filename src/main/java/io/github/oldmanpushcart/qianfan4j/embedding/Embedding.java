package io.github.oldmanpushcart.qianfan4j.embedding;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 向量结算结果
 *
 * @param index  结果编号，与{@link EmbeddingRequest#texts()}中的顺序对应
 * @param vector 文档向量
 */
public record Embedding(

        @JsonProperty("index")
        int index,

        @JsonProperty("embedding")
        float[] vector

) implements Comparable<Embedding> {

    @Override
    public int compareTo(Embedding o) {
        return Integer.compare(index, o.index);
    }

}
