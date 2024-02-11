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
                .model(new PluginModel("ujwdafb5hz1e4qee"))
                .plugins(Plugin.KNOWLEDGE_BASE, Plugin.CHAT_OCR)
                .question("解析这张图片")
                .imageUrl(new URL("https://erniebot4j-image.bj.bcebos.com/bfa05c91-99ed-419f-bcca-06823f3bcb4b.png"))
                .build();

        final var response = client.plugin(request)
                .async()
                .join();

        System.out.println(response);
    }

}
