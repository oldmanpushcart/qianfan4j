package io.github.ompc.erniebot4j.test;

import io.github.ompc.erniebot4j.pluginapp.Plugin;
import io.github.ompc.erniebot4j.pluginapp.PluginAppModel;
import io.github.ompc.erniebot4j.pluginapp.PluginAppRequest;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class DebugTestCase implements LoadingProperties {

    @Test
    public void test$debug() throws MalformedURLException {

        final var request = new PluginAppRequest.Builder()
                .model(new PluginAppModel(PLUGIN_ENDPOINT))
                .plugins(Plugin.CHAT_OCR)
                .question("解析这个文件")
                .fileUrl(new URL("https://erniebot4j-image.bj.bcebos.com/image-003.jpeg"))
                .build();

        final var response = client.pluginApp(request)
                .async()
                .join();

        System.out.println(response);
    }

}
