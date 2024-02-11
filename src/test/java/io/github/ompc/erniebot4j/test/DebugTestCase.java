package io.github.ompc.erniebot4j.test;

import io.github.ompc.erniebot4j.chat.ChatModel;
import io.github.ompc.erniebot4j.chat.ChatRequest;
import io.github.ompc.erniebot4j.chat.message.Message;
import org.junit.Test;

public class DebugTestCase implements LoadingProperties {

    @Test
    public void test$debug() {
        final var request = new ChatRequest.Builder()
                .model(ChatModel.ERNIEBOT_V4)
                .message(Message.human("""
                        你可以解读图片链接中的图片吗？
                        """))
                //.option(ChatOptions.IS_ENABLE_SEARCH, false)
                //.option(ChatOptions.IS_ENABLE_CITATION, true)
                //.function(new EchoFunction())
                .build();
        final var response = client.chat(request)
                .async()
                .join();
        System.out.println(response.sentence().content());
    }

}
