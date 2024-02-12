package io.github.ompc.erniebot4j.embedding;

/**
 * 向量结算结果
 *
 * @param index  结果编号，与{@link EmbeddingRequest#documents()}中的顺序对应
 * @param vector 文档向量
 */
public record Embedding(int index, float[] vector) implements Comparable<Embedding> {

    @Override
    public int compareTo(Embedding o) {
        return index - o.index;
    }

}
