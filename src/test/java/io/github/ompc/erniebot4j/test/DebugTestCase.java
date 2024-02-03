package io.github.ompc.erniebot4j.test;

import io.github.ompc.erniebot4j.ErnieBotClient;
import io.github.ompc.erniebot4j.chat.ChatModel;
import io.github.ompc.erniebot4j.chat.ChatOptions;
import io.github.ompc.erniebot4j.chat.ChatRequest;
import io.github.ompc.erniebot4j.chat.message.Message;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DebugTestCase implements LoadingProperties {

    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    @AfterClass
    public static void clean() {
        executor.shutdown();
    }

    @Test
    public void test$debug() {
        final var request = new ChatRequest.Builder()
                .model(ChatModel.ERNIEBOT_8K)
                .build();
        request.messages().add(Message.human("北京和上海的天气"));
        request.options()
                .option(ChatOptions.IS_STREAM, true)
                .option(ChatOptions.IS_ENABLE_SEARCH, true)
                .option(ChatOptions.IS_ENABLE_CITATION, true)
        ;
        final var response = client.chat(request)
                .async()
                .join();
        System.out.println(response);
    }

}
