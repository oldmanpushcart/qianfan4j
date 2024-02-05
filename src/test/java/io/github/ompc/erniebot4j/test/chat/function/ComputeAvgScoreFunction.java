package io.github.ompc.erniebot4j.test.chat.function;

import io.github.ompc.erniebot4j.chat.function.ChatFn;
import io.github.ompc.erniebot4j.chat.function.ChatFnExample;
import io.github.ompc.erniebot4j.chat.function.ChatFunction;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@ChatFn(name = "compute_avg_score", description = "计算平均成绩", resource = "/schema/compute_avg_score.schema.json")
@ChatFnExample(
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
)
public class ComputeAvgScoreFunction implements ChatFunction<ComputeAvgScoreFunction.Request, ComputeAvgScoreFunction.Response> {

    @Override
    public CompletableFuture<Response> call(Request request) {
        return CompletableFuture.completedFuture(new Response(
                (float) Stream.of(request.scores()).mapToDouble(Float::doubleValue).average().orElse(0)
        ));
    }

    public record Request(Float[] scores) {

    }

    public record Response(Float avgScore) {

    }

}
