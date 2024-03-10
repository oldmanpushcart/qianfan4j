package io.github.oldmanpushcart.test.qianfan4j.pluginapp;

import io.github.oldmanpushcart.qianfan4j.pluginapp.Plugin;
import io.github.oldmanpushcart.qianfan4j.pluginapp.PluginAppModel;
import io.github.oldmanpushcart.qianfan4j.pluginapp.PluginAppOptions;
import io.github.oldmanpushcart.qianfan4j.pluginapp.PluginAppRequest;
import io.github.oldmanpushcart.test.qianfan4j.LoadingEnv;
import io.github.oldmanpushcart.test.qianfan4j.QianFanAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;

public class PluginAppTestCase implements LoadingEnv {

    @Test
    public void test$plugin_app$kb() {

        final var request = PluginAppRequest.newBuilder()
                .model(new PluginAppModel(PLUGIN_APP_ID))
                .plugins(Plugin.KNOWLEDGE_BASE, Plugin.CHAT_OCR)
                .question("请告诉我哈利波特和罗恩是否情侣关系，你只需要回答YES或者NO")
                .option(PluginAppOptions.IS_STREAM, true)
                .option(PluginAppOptions.IS_VERBOSE, true)
                .build();

        final var response = client.pluginApp(request)
                .async()
                .join();

        QianFanAssertions.assertAlgoResponse(response);
        Assertions.assertNotNull(response.meta());
        Assertions.assertFalse(response.meta().pluginId().isBlank());
        Assertions.assertFalse(response.meta().raw().request().isBlank());
        Assertions.assertFalse(response.meta().raw().response().isBlank());
        Assertions.assertTrue(response.content().contains("NO"));

    }

    @Test
    public void test$plugin_app$ocr_image$001() {

        final var request = PluginAppRequest.newBuilder()
                .model(new PluginAppModel(PLUGIN_APP_ID))
                .plugins(Plugin.KNOWLEDGE_BASE, Plugin.CHAT_OCR)
                .question("请告诉我照片上女人是否有戴口罩，你只需要回答YES或者NO")
                .fileUri(URI.create("https://erniebot4j-image.bj.bcebos.com/image-001.jpeg"))
                .option(PluginAppOptions.IS_STREAM, true)
                .option(PluginAppOptions.IS_VERBOSE, true)
                .build();

        final var response = client.pluginApp(request)
                .async()
                .join();

        QianFanAssertions.assertAlgoResponse(response);
        Assertions.assertNotNull(response.meta());
        Assertions.assertFalse(response.meta().pluginId().isBlank());
        Assertions.assertFalse(response.meta().raw().request().isBlank());
        Assertions.assertFalse(response.meta().raw().response().isBlank());
        Assertions.assertTrue(response.content().contains("NO"));

    }

    @Test
    public void test$plugin_app$ocr_image$002() {

        final var request = PluginAppRequest.newBuilder()
                .model(new PluginAppModel(PLUGIN_APP_ID))
                .plugins(Plugin.KNOWLEDGE_BASE, Plugin.CHAT_OCR)
                .question("请告诉我照片上是否有自行车，你只需要回答YES或者NO")
                .fileUri(URI.create("https://erniebot4j-image.bj.bcebos.com/image-002.jpeg"))
                .option(PluginAppOptions.IS_STREAM, true)
                .option(PluginAppOptions.IS_VERBOSE, true)
                .build();

        final var response = client.pluginApp(request)
                .async()
                .join();

        QianFanAssertions.assertAlgoResponse(response);
        Assertions.assertNotNull(response.meta());
        Assertions.assertFalse(response.meta().pluginId().isBlank());
        Assertions.assertFalse(response.meta().raw().request().isBlank());
        Assertions.assertFalse(response.meta().raw().response().isBlank());
        Assertions.assertTrue(response.content().contains("YES"));

    }

    @Test
    public void test$plugin_app$ocr_image$003() {

        final var request = PluginAppRequest.newBuilder()
                .model(new PluginAppModel(PLUGIN_APP_ID))
                .plugins(Plugin.KNOWLEDGE_BASE, Plugin.CHAT_OCR)
                .question("图片中是什么？")
                .fileUri(URI.create("https://erniebot4j-image.bj.bcebos.com/image-003.jpeg"))
                .option(PluginAppOptions.IS_VERBOSE, true)
                .build();

        final var response = client.pluginApp(request)
                .async()
                .join();

        QianFanAssertions.assertAlgoResponse(response);
        Assertions.assertNotNull(response.meta());
        Assertions.assertFalse(response.meta().pluginId().isBlank());
        Assertions.assertFalse(response.meta().raw().request().isBlank());
        Assertions.assertFalse(response.meta().raw().response().isBlank());
        Assertions.assertTrue(response.content().contains("避孕套"));

    }

}
