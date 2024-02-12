package io.github.ompc.erniebot4j.test;

import io.github.ompc.erniebot4j.ErnieBotClient;
import io.github.ompc.erniebot4j.TokenRefresher;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface LoadingProperties {

    Properties prop = new Properties() {{
        try (final var input = new FileInputStream(System.getProperties().getProperty("erniebot.properties.file"))) {
            load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }};

    TokenRefresher refresher = new TokenRefresher(
            prop.getProperty("erniebot.qianfan.ak"),
            prop.getProperty("erniebot.qianfan.sk")
    );

    ExecutorService executor = Executors.newFixedThreadPool(10);

    ErnieBotClient client = new ErnieBotClient.Builder()
            .refresher(refresher)
            .executor(executor)
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    String PLUGIN_ENDPOINT = prop.getProperty("erniebot.plugin.endpoint");

}
