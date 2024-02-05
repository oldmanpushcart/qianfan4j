package io.github.ompc.erniebot4j.test;

import io.github.ompc.erniebot4j.chat.ChatModel;
import io.github.ompc.erniebot4j.chat.ChatOptions;
import io.github.ompc.erniebot4j.chat.ChatRequest;
import io.github.ompc.erniebot4j.chat.message.Message;
import org.junit.Test;

public class DebugTestCase implements LoadingProperties {

    @Test
    public void test$debug() {
        final var request = new ChatRequest.Builder()
                .model(ChatModel.ERNIEBOT_8K)
                .message(Message.human("北京和上海的天气"))
                .option(ChatOptions.IS_STREAM, true)
                .option(ChatOptions.IS_ENABLE_SEARCH, true)
                .option(ChatOptions.IS_ENABLE_CITATION, true)
                .build();
        final var response = client.chat(request)
                .async()
                .join();
        System.out.println(response);
    }

}
