package io.github.ompc.erniebot4j.embedding;

import io.github.ompc.erniebot4j.executor.Model;

/**
 * 向量计算模型
 *
 * @param name   模型名称
 * @param remote 模型远程地址
 */
public record EmbeddingModel(String name, String remote) implements Model {

    public static final EmbeddingModel EMBEDDING_V1 = new EmbeddingModel(
            "embedding-1.0",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/embeddings/embedding-v1"
    );

    public static final EmbeddingModel BGE_LARGE_ZH = new EmbeddingModel(
            "bge-large-zh",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/embeddings/bge_large_zh"
    );

    public static final EmbeddingModel BGE_LARGE_EN = new EmbeddingModel(
            "bge-large-en",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/embeddings/bge_large_en"
    );

    public static final EmbeddingModel TAO_8K = new EmbeddingModel(
            "tao-8k",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/embeddings/tao_8k"
    );

}
