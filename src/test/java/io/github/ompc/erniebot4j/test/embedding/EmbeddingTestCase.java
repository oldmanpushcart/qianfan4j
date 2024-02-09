package io.github.ompc.erniebot4j.test.embedding;

import io.github.ompc.erniebot4j.embedding.EmbeddingModel;
import io.github.ompc.erniebot4j.embedding.EmbeddingRequest;
import io.github.ompc.erniebot4j.test.ErnieBotAssert;
import io.github.ompc.erniebot4j.test.LoadingProperties;
import org.junit.Assert;
import org.junit.Test;

public class EmbeddingTestCase implements LoadingProperties {

    @Test
    public void test$embedding() {

        final var request = new EmbeddingRequest.Builder()
                .model(EmbeddingModel.EMBEDDING_V1)
                .documents(
                        "推荐一些美食",
                        "给我讲个故事"
                )
                .build();

        final var response = client.embedding(request)
                .async()
                .join();

        ErnieBotAssert.assertResponse(response);
        Assert.assertEquals("embedding_list", response.type());
        Assert.assertNotNull(response.usage());
        Assert.assertEquals(2, response.embeddings().length);
        for (final var embedding : response.embeddings()) {
            Assert.assertNotNull(embedding);
            Assert.assertTrue(embedding.vectors().length > 0);
            System.out.println(embedding);
        }
    }

}
