package io.github.oldmanpushcart.test.qianfan4j.embedding;

import io.github.oldmanpushcart.qianfan4j.embedding.EmbeddingModel;
import io.github.oldmanpushcart.qianfan4j.embedding.EmbeddingRequest;
import io.github.oldmanpushcart.test.qianfan4j.LoadingEnv;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EmbeddingTestCase implements LoadingEnv {

    @Test
    public void test$embedding() {

        final var request = EmbeddingRequest.newBuilder()
                .model(EmbeddingModel.EMBEDDING_V1)
                .texts(
                        "推荐一些美食",
                        "给我讲个故事"
                )
                .build();

        final var response = client.embedding(request)
                .async()
                .join();

        Assertions.assertEquals(2, response.embeddings().size());
        for (final var embedding : response.embeddings()) {
            Assertions.assertNotNull(embedding);
            Assertions.assertTrue(embedding.vector().length > 0);
        }

    }

}
