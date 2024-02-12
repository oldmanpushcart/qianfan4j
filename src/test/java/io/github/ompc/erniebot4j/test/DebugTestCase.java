package io.github.ompc.erniebot4j.test;

import io.github.ompc.erniebot4j.plugin.Plugin;
import io.github.ompc.erniebot4j.plugin.PluginModel;
import io.github.ompc.erniebot4j.plugin.PluginRequest;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class DebugTestCase implements LoadingProperties {

    @Test
    public void test$debug() throws MalformedURLException {

        final var request = new PluginRequest.Builder()
                .model(new PluginModel(PLUGIN_ENDPOINT))
                .plugins(Plugin.CHAT_OCR)
                .question("解析这个文件")
                .fileUrl(new URL("https://erniebot4j-image.bj.bcebos.com/image-003.jpeg"))
                .build();

        final var response = client.plugin(request)
                .async()
                .join();

        System.out.println(response);
    }

}
