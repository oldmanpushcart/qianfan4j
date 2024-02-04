package io.github.ompc.erniebot4j.test.chat.function;

import io.github.ompc.erniebot4j.chat.function.ChatFn;
import io.github.ompc.erniebot4j.chat.function.ChatFnExample;
import io.github.ompc.erniebot4j.chat.function.ChatFnSchemaResource;
import io.github.ompc.erniebot4j.chat.function.ChatFunction;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@ChatFn(name = "compute_avg_score", description = "计算平均成绩")
@ChatFnSchemaResource(
        parameters = "/schema/compute_avg_score.parameters.json",
        responses = "/schema/compute_avg_score.responses.json"
)
@ChatFnExample(
        question = "计算张三的平均成绩",
        arguments = """
                {
                     "scores": [
                         90,
                         80,
                         70
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
