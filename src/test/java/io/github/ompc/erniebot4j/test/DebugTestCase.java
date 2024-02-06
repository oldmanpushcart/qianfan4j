package io.github.ompc.erniebot4j.test;

import io.github.ompc.erniebot4j.chat.ChatModel;
import io.github.ompc.erniebot4j.chat.ChatRequest;
import io.github.ompc.erniebot4j.chat.message.Message;
import io.github.ompc.erniebot4j.test.chat.function.EchoFunction;
import org.junit.Test;

public class DebugTestCase implements LoadingProperties {

    @Test
    public void test$debug() {
        final var request = new ChatRequest.Builder()
                .model(ChatModel.ERNIEBOT_8K)
                .message(Message.human("echo: hello world!"))
                .function(new EchoFunction())
                .build();
        final var response = client.chat(request)
                .async()
                .join();
        System.out.println(response);
    }

}
