package io.github.oldmanpushcart.test.qianfan4j;

import io.github.oldmanpushcart.qianfan4j.pluginapp.Plugin;
import io.github.oldmanpushcart.qianfan4j.pluginapp.PluginAppModel;
import io.github.oldmanpushcart.qianfan4j.pluginapp.PluginAppOptions;
import io.github.oldmanpushcart.qianfan4j.pluginapp.PluginAppRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;

@Disabled
public class DebugTestCase implements LoadingEnv {

    @Test
    public void test$debug() {

        final var request = PluginAppRequest.newBuilder()
                .model(new PluginAppModel(PLUGIN_APP_ID))
                .plugins(Plugin.KNOWLEDGE_BASE, Plugin.CHAT_OCR)
                .question("请告诉我照片上有几辆自行车?")
                .fileUri(URI.create("https://erniebot4j-image.bj.bcebos.com/image-002.jpeg"))
                .option(PluginAppOptions.IS_STREAM, true)
                .option(PluginAppOptions.IS_VERBOSE, true)
                .build();

        final var response = client.pluginApp(request)
                .async()
                .join();

        System.out.println(response.content());

    }

}
