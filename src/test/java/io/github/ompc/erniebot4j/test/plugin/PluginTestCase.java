package io.github.ompc.erniebot4j.test.plugin;

import io.github.ompc.erniebot4j.plugin.Plugin;
import io.github.ompc.erniebot4j.plugin.PluginModel;
import io.github.ompc.erniebot4j.plugin.PluginOptions;
import io.github.ompc.erniebot4j.plugin.PluginRequest;
import io.github.ompc.erniebot4j.test.ErnieBotAssert;
import io.github.ompc.erniebot4j.test.LoadingProperties;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PluginTestCase implements LoadingProperties {

    @Test
    public void test$plugin$kb() {

        final var request = new PluginRequest.Builder()
                .model(new PluginModel("ujwdafb5hz1e4qee"))
                .question("请告诉我哈利波特和罗恩是否情侣关系，你只需要回答YES或者NO")
                .plugins(Plugin.KNOWLEDGE_BASE, Plugin.CHAT_OCR)
                .option(PluginOptions.IS_STREAM, true)
                .option(PluginOptions.IS_VERBOSE, true)
                .build();

        final var response = client.plugin(request)
                .async()
                .join();

        ErnieBotAssert.assertResponse(response);
        ErnieBotAssert.assertSentence(response.sentence());
        Assert.assertTrue(response.sentence().content().contains("NO"));

    }

    @Test
    public void test$plugin$ocr_image$001() throws IOException {

        final var imageUrl = bos.putPngImage(
                "erniebot4j-image",
                "%s.png".formatted(UUID.randomUUID()),
                ImageIO.read(new File("./ocr-image/image-001.jpeg"))
        ).join();

        final var request = new PluginRequest.Builder()
                .model(new PluginModel("ujwdafb5hz1e4qee"))
                .plugins(Plugin.KNOWLEDGE_BASE, Plugin.CHAT_OCR)
                .question("请告诉我照片上女人是否有戴口罩，你只需要回答YES或者NO")
                .imageUrl(imageUrl)
                .option(PluginOptions.IS_STREAM, true)
                .option(PluginOptions.IS_VERBOSE, true)
                .build();

        final var response = client.plugin(request)
                .async()
                .join();

        ErnieBotAssert.assertResponse(response);
        ErnieBotAssert.assertSentence(response.sentence());
        Assert.assertTrue(response.sentence().content().contains("NO"));

    }

    @Test
    public void test$plugin$ocr_image$002() throws IOException {

        final var imageUrl = bos.putPngImage(
                "erniebot4j-image",
                "%s.png".formatted(UUID.randomUUID()),
                ImageIO.read(new File("./ocr-image/image-002.jpg"))
        ).join();

        final var request = new PluginRequest.Builder()
                .model(new PluginModel("ujwdafb5hz1e4qee"))
                .question("请告诉我照片上是否有自行车，你只需要回答YES或者NO")
                .imageUrl(imageUrl)
                .plugins(Plugin.KNOWLEDGE_BASE, Plugin.CHAT_OCR)
                .option(PluginOptions.IS_STREAM, true)
                .option(PluginOptions.IS_VERBOSE, true)
                .build();

        final var response = client.plugin(request)
                .async()
                .join();

        ErnieBotAssert.assertResponse(response);
        ErnieBotAssert.assertSentence(response.sentence());
        Assert.assertTrue(response.sentence().content().contains("YES"));

    }

}
