package io.github.ompc.erniebot4j.test;

import io.github.ompc.erniebot4j.ErnieBotClient;
import io.github.ompc.erniebot4j.TokenRefresher;
import io.github.ompc.erniebot4j.bce.BceCredential;
import io.github.ompc.erniebot4j.bce.bos.BosClient;

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
            prop.getProperty("erniebot.identity"),
            prop.getProperty("erniebot.secret")
    );

    BceCredential credential = new BceCredential(
            prop.getProperty("erniebot.bce.bos.ak"),
            prop.getProperty("erniebot.bce.bos.sk")
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
