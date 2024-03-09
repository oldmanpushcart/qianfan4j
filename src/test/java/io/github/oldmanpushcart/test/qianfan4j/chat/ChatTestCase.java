package io.github.oldmanpushcart.test.qianfan4j.chat;

import io.github.oldmanpushcart.qianfan4j.chat.ChatModel;
import io.github.oldmanpushcart.qianfan4j.chat.ChatRequest;
import io.github.oldmanpushcart.qianfan4j.chat.message.Message;
import io.github.oldmanpushcart.test.qianfan4j.LoadingEnv;
import io.github.oldmanpushcart.test.qianfan4j.chat.function.EchoFunction;
import org.junit.jupiter.api.Test;

public class ChatTestCase implements LoadingEnv {

    @Test
    public void test$chat$function() {

        final var request = ChatRequest.newBuilder()
                .model(ChatModel.ERNIEBOT_8K)
                .functions(new EchoFunction())
                .messages(Message.ofUser("echo: HELLO WORLD!"))
                .build();

        final var response = client.chat(request).async().join();
        System.out.println(response);

    }

}
