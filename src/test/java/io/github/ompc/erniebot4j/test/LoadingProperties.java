package io.github.ompc.erniebot4j.test;

import io.github.ompc.erniebot4j.TokenRefresher;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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

}
