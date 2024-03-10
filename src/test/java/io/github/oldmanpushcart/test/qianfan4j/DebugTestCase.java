package io.github.oldmanpushcart.test.qianfan4j;

import io.github.oldmanpushcart.qianfan4j.chat.ChatModel;
import io.github.oldmanpushcart.qianfan4j.chat.ChatRequest;
import io.github.oldmanpushcart.qianfan4j.chat.message.Message;
import io.github.oldmanpushcart.test.qianfan4j.chat.function.EchoFunction;
import org.junit.jupiter.api.Test;

public class DebugTestCase implements LoadingEnv {

    @Test
    public void test$debug() {

        final var request = ChatRequest.newBuilder()
                .model(ChatModel.ERNIE_V4)
                .functions(new EchoFunction())
                .messages(Message.ofUser("echo: HELLO WORLD!"))
                .build();

        final var response = client.chat(request)
                .async()
                .join();

        System.out.println(response.content());

    }

}
