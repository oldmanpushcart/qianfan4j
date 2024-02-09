package io.github.ompc.erniebot4j.test;

import io.github.ompc.erniebot4j.executor.Response;
import io.github.ompc.erniebot4j.executor.Sentence;
import io.github.ompc.erniebot4j.executor.Usage;
import org.junit.Assert;

public class ErnieBotAssert {

    public static void assertResponse(Response response) {

        Assert.assertNotNull(response);
        Assert.assertTrue(response.timestamp() > 0);

        Assert.assertNotNull(response.id());
        Assert.assertFalse(response.id().isBlank());

        Assert.assertNotNull(response.type());
        Assert.assertFalse(response.type().isBlank());

        Assert.assertNotNull(response.usage());
        assertUsage(response.usage());

    }

    public static void assertUsage(Usage usage) {
        Assert.assertNotNull(usage);
        Assert.assertTrue(usage.completion() >= 0);
        Assert.assertTrue(usage.prompt() >= 0);
        Assert.assertTrue(usage.total() >= 0);
        Assert.assertEquals(usage.total(), usage.completion() + usage.prompt());
    }

    public static void assertSentence(Sentence sentence) {
        Assert.assertNotNull(sentence);
        Assert.assertTrue(sentence.index() >= 0);
        Assert.assertNotNull(sentence.content());
        Assert.assertFalse(sentence.content().isEmpty());
    }

}
