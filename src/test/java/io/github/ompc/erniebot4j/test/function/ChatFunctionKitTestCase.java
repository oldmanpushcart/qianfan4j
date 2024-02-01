package io.github.ompc.erniebot4j.test.function;

import io.github.ompc.erniebot4j.chat.function.ChatFunctionKit;
import org.junit.Assert;
import org.junit.Test;

public class ChatFunctionKitTestCase {

    @Test
    public void test$kit$base() {

        final var kit = new ChatFunctionKit()
                .load(new QueryScoreFunction())
                .load(new ComputeAvgScoreFunction());

        Assert.assertEquals(2, kit.size());
        Assert.assertEquals(2, kit.all().size());

        for (final var stub : kit) {
            Assert.assertNotNull(stub);
            Assert.assertNotNull(stub.function());
            Assert.assertNotNull(stub.meta());
            Assert.assertNotNull(stub.meta().schema());
            Assert.assertNotNull(stub.meta().name());
            Assert.assertNotNull(stub.meta().description());
            Assert.assertNotNull(stub.meta().method());
            Assert.assertNotNull(stub.meta().parameterType());
            Assert.assertNotNull(stub.meta().returnType());
        }

        Assert.assertTrue(kit.contains("query_score"));
        Assert.assertTrue(kit.contains("compute_avg_score"));
        Assert.assertFalse(kit.contains("not_existed_function_name"));
        Assert.assertFalse(kit.isEmpty());
        Assert.assertNotNull(kit.get("query_score"));
        Assert.assertNotNull(kit.require("query_score"));
        Assert.assertEquals(2, kit.requires("query_score", "compute_avg_score").size());
        Assert.assertEquals(2, kit.search("query_score", "compute_avg_score", "not_existed_function_name").size());

    }

}
