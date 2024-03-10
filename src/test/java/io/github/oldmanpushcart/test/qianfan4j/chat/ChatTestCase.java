package io.github.oldmanpushcart.test.qianfan4j.chat;

import io.github.oldmanpushcart.qianfan4j.chat.ChatModel;
import io.github.oldmanpushcart.qianfan4j.chat.ChatOptions;
import io.github.oldmanpushcart.qianfan4j.chat.ChatRequest;
import io.github.oldmanpushcart.qianfan4j.chat.message.Message;
import io.github.oldmanpushcart.test.qianfan4j.LoadingEnv;
import io.github.oldmanpushcart.test.qianfan4j.QianFanAssertions;
import io.github.oldmanpushcart.test.qianfan4j.chat.function.ComputeAvgScoreFunction;
import io.github.oldmanpushcart.test.qianfan4j.chat.function.EchoFunction;
import io.github.oldmanpushcart.test.qianfan4j.chat.function.QueryScoreFunction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ChatTestCase implements LoadingEnv {

    @Test
    public void test$chat$function$multi() {

        final var request = ChatRequest.newBuilder()
                .model(ChatModel.ERNIE_V4)
                .functions(new QueryScoreFunction(), new ComputeAvgScoreFunction())
                .option(ChatOptions.IS_STREAM, true)
                .option(ChatOptions.IS_ENABLE_SEARCH, false)
                .option(ChatOptions.TEMPERATURE, 0.01f)
                .messages(Message.ofUser("计算李四的语文和数学平均分"))
                .build();

        final var response = client.chat(request).async().join();
        Assertions.assertTrue(response.content().contains("75"));
        Assertions.assertTrue(response.isSafe());
        Assertions.assertTrue(response.isLast());
        Assertions.assertTrue(response.ret().isSuccess());
        QianFanAssertions.assertAlgoResponse(response);

    }

    @Test
    public void test$chat$function() {

        final var request = ChatRequest.newBuilder()
                .model(ChatModel.ERNIE_V4)
                .functions(new EchoFunction())
                .messages(Message.ofUser("echo: HELLO WORLD!"))
                .build();

        final var response = client.chat(request).async().join();
        Assertions.assertTrue(response.content().contains("HELLO WORLD!"));
        QianFanAssertions.assertAlgoResponse(response);

    }

}
