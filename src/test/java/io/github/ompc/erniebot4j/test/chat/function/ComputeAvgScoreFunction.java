package io.github.ompc.erniebot4j.test.chat.function;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.ompc.erniebot4j.chat.function.ChatFn;
import io.github.ompc.erniebot4j.chat.function.ChatFnExample;
import io.github.ompc.erniebot4j.chat.function.ChatFunction;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@ChatFn(name = "compute_avg_score", description = "计算平均成绩")
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

    @JsonClassDescription("请求")
    public record Request(
            @JsonProperty(required = true)
            @JsonPropertyDescription("分数集合")
            Float[] scores
    ) {

    }

    @JsonClassDescription("响应")
    public record Response(
            @JsonPropertyDescription("平均分")
            Float avgScore
    ) {

    }

}
