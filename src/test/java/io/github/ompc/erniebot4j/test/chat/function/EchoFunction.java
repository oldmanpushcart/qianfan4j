package io.github.ompc.erniebot4j.test.chat.function;

import io.github.ompc.erniebot4j.chat.function.ChatFn;
import io.github.ompc.erniebot4j.chat.function.ChatFnExample;
import io.github.ompc.erniebot4j.chat.function.ChatFunction;

import java.util.concurrent.CompletableFuture;

@ChatFn(name = "echo", description = "echo words")
@ChatFnExample(
        question = "echo: words",
        thoughts = "当用户输入echo:开头的消息时，机器人会原样返回用户输入的消息",
        arguments = """
                {
                    "words": "hello, world"
                }
                """
)
public class EchoFunction implements ChatFunction<EchoFunction.Echo, EchoFunction.Echo> {

    @Override
    public CompletableFuture<Echo> call(Echo echo) {
        return CompletableFuture.completedFuture(new Echo(echo.words()));
    }

    public record Echo(String words) {

    }

}
