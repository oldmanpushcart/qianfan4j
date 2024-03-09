package io.github.oldmanpushcart.test.qianfan4j.chat.function;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.oldmanpushcart.qianfan4j.chat.function.ChatFn;
import io.github.oldmanpushcart.qianfan4j.chat.function.ChatFunction;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@ChatFn(name = "compute_avg_score", description = "计算平均成绩", examples = {
        @ChatFn.Example(
                question = """
                        张三的语文30分、数学20分、英语100分；
                        李四的语文50分、数学90分、英语60分；
                        计算张三的平均成绩
                        """,
                thoughts = "我应该将张三的所有分数传入，计算张三的平均分",
                arguments = """
                        {
                             "scores": [
                                 30,
                                 20,
                                 100
                             ]
                         }
                        """
        ),
        @ChatFn.Example(
                question = "张三的数学成绩是50分、语文30分、英语20分；李四的数学成绩是60分、语文90分；请计算他们的语文平均成绩",
                thoughts = "我应该把所有人的语文分数传入，从而计算出语文的平均成绩",
                arguments = """
                        {
                             "scores": [
                                 30,
                                 90
                             ]
                         }
                        """
        )
})
public class ComputeAvgScoreFunction implements ChatFunction<ComputeAvgScoreFunction.Request, ComputeAvgScoreFunction.Response> {

    @Override
    public CompletableFuture<Response> call(Request request) {
        return CompletableFuture.completedFuture(new Response(
                (float) Stream.of(request.scores()).mapToDouble(Float::doubleValue).average().orElse(0)
        ));
    }

    public record Request(
            @JsonPropertyDescription("分数集合")
            Float[] scores
    ) {

    }

    public record Response(
            @JsonPropertyDescription("平均分")
            Float avgScore
    ) {

    }

}