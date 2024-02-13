package io.github.ompc.erniebot4j.embedding;

import io.github.ompc.erniebot4j.executor.Response;
import io.github.ompc.erniebot4j.executor.Usage;

/**
 * 向量计算应答
 *
 * @param id         应答ID
 * @param type       应答类型
 * @param timestamp  应答时间戳
 * @param usage      应答用量
 * @param embeddings 计算结果信息，与{@link EmbeddingRequest#texts()}中的顺序对应
 */
public record EmbeddingResponse(
        String id,
        String type,
        long timestamp,
        Usage usage,
        Embedding[] embeddings
) implements Response {

}
