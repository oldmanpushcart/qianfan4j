package io.github.oldmanpushcart.qianfan4j.embedding;

import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoResponse;

import java.util.List;

/**
 * 向量计算应答
 */
public interface EmbeddingResponse extends AlgoResponse {

    /**
     * 获取向量
     * <p>与{@link EmbeddingRequest#texts()}中的顺序对应</p>
     *
     * @return 向量
     */
    List<Embedding> embeddings();

}
