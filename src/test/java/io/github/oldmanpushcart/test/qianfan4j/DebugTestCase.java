package io.github.oldmanpushcart.test.qianfan4j;

import io.github.oldmanpushcart.qianfan4j.chat.ChatModel;
import io.github.oldmanpushcart.qianfan4j.chat.ChatRequest;
import io.github.oldmanpushcart.qianfan4j.chat.message.Message;
import org.junit.jupiter.api.Test;

public class DebugTestCase implements LoadingEnv {

    @Test
    public void test$debug() {

        final var request = ChatRequest.newBuilder()
                .model(ChatModel.ERNIE_V4)
                .messages(Message.ofUser("hello!"))
                .build();

        final var response = client.chat(request).async().join();
        System.out.println(response);

    }

}
