package io.github.oldmanpushcart.test.qianfan4j;

import io.github.oldmanpushcart.qianfan4j.QianFanClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public interface LoadingEnv {

    String AK = System.getenv("QIANFAN_AK");
    String SK = System.getenv("QIANFAN_SK");
    String PLUGIN_APP_ID = System.getenv("QIANFAN_PLUGIN_APP_ID");

    ExecutorService executor = Executors.newFixedThreadPool(10);

    QianFanClient client = QianFanClient.newBuilder()
            .ak(AK)
            .sk(SK)
            .executor(executor)
            .build();

}
