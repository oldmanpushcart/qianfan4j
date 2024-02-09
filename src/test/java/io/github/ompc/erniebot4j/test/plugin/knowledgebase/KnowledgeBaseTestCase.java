package io.github.ompc.erniebot4j.test.plugin.knowledgebase;

import io.github.ompc.erniebot4j.plugin.Plugin;
import io.github.ompc.erniebot4j.plugin.knowledgebase.KnowledgeBaseOptions;
import io.github.ompc.erniebot4j.plugin.knowledgebase.KnowledgeBaseRequest;
import io.github.ompc.erniebot4j.plugin.knowledgebase.KnowledgeBaseResponse;
import io.github.ompc.erniebot4j.plugin.knowledgebase.KnowledgeModel;
import io.github.ompc.erniebot4j.test.ErnieBotAssert;
import io.github.ompc.erniebot4j.test.LoadingProperties;
import org.junit.Assert;
import org.junit.Test;

public class KnowledgeBaseTestCase implements LoadingProperties {

    @Test
    public void test$kb() {

        final var endpoint = "ujwdafb5hz1e4qee";
        final var request = new KnowledgeBaseRequest.Builder()
                .model(new KnowledgeModel(endpoint))
                .query("福尔摩斯和狄仁杰的关系？")
                .option(KnowledgeBaseOptions.IS_STREAM, true)
                .option(KnowledgeBaseOptions.IS_VERBOSE, true)
                .option(KnowledgeBaseOptions.IS_ENABLE_SEARCH, false)
                .build();

        final var response = client.plugin().kb(request)
                .async()
                .join();

        ErnieBotAssert.assertResponse(response);
        ErnieBotAssert.assertSentence(response.sentence());
        Assert.assertEquals("chat.completion", response.type());
        Assert.assertTrue(response.logId() > 0);
        Assert.assertNotNull(response.meta());
        assertMeta(response.meta());
        assertMeta(response.meta());

        System.out.println(response);

    }

    private static void assertMeta(KnowledgeBaseResponse.Meta meta) {
        Assert.assertNotNull(meta);

        Assert.assertNotNull(meta.plugin());
        Assert.assertEquals(Plugin.KNOWLEDGE_BASE, meta.plugin());

        Assert.assertNotNull(meta.request());
        Assert.assertFalse(meta.request().query().isBlank());
        Assert.assertTrue(meta.request().topN() >= 0);
        Assert.assertTrue(meta.request().score() >= 0);
        Assert.assertTrue(meta.request().kbs().length > 0);

        Assert.assertEquals(1, meta.response().code());
        Assert.assertTrue(meta.response().message().isBlank());

        Assert.assertNotNull(meta.response().cost());
        Assert.assertTrue(meta.response().cost().bes() >= 0);
        Assert.assertTrue(meta.response().cost().db() >= 0);
        Assert.assertTrue(meta.response().cost().embedded() >= 0);
        Assert.assertTrue(meta.response().cost().signed() >= 0);

        Assert.assertNotNull(meta.response().documents());
        Assert.assertTrue(meta.response().documents().length > 0);
        for (final var document : meta.response().documents()) {
            Assert.assertNotNull(document);
            Assert.assertFalse(document.download().isBlank());
            Assert.assertFalse(document.id().isBlank());
            Assert.assertFalse(document.name().isBlank());
            Assert.assertFalse(document.kbId().isBlank());
            Assert.assertTrue(document.score() >= 0);
            Assert.assertFalse(document.content().isBlank());
            Assert.assertNotNull(document.shard());
            Assert.assertFalse(document.shard().id().isBlank());
            Assert.assertTrue(document.shard().index() >= 0);
        }
        Assert.assertNotNull(meta.response());

    }

}
