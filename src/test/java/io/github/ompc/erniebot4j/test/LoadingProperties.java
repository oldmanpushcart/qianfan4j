package io.github.ompc.erniebot4j.test;

import io.github.ompc.erniebot4j.ErnieBotClient;
import io.github.ompc.erniebot4j.TokenRefresher;
import io.github.ompc.erniebot4j.cloud.baidu.BceCredential;
import io.github.ompc.erniebot4j.cloud.baidu.bos.BosClient;

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

    BceCredential credential = new BceCredential(
            prop.getProperty("erniebot.cloud.baidu.ak"),
            prop.getProperty("erniebot.cloud.baidu.sk")
    );

    ExecutorService executor = Executors.newFixedThreadPool(10);

    ErnieBotClient client = new ErnieBotClient.Builder()
            .refresher(refresher)
            .executor(executor)
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    BosClient bos = new BosClient.Builder()
            .credential(credential)
            .executor(executor)
            .build();

}
