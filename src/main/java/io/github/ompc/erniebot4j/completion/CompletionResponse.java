package io.github.ompc.erniebot4j.completion;

import io.github.ompc.erniebot4j.executor.Aggregatable;
import io.github.ompc.erniebot4j.executor.Response;
import io.github.ompc.erniebot4j.executor.Sentence;
import io.github.ompc.erniebot4j.executor.Usage;

import java.util.Optional;

public record CompletionResponse(
        String id,
        String type,
        long timestamp,
        Usage usage,
        Sentence sentence
) implements Response, Aggregatable<CompletionResponse> {

    @Override
    public CompletionResponse aggregate(CompletionResponse response) {
        return Optional.ofNullable(response)
                .map(other -> new CompletionResponse(
                        this.id(),
                        this.type(),
                        this.timestamp(),
                        new Usage(
                                other.usage().prompt(),
                                // completion的usage有点特殊，它的completion是两个completion的和
                                this.usage().completion() + other.usage().completion(),
                                other.usage().total()
                        ),
                        new Sentence(
                                this.sentence().index(),
                                this.sentence().isLast() || other.sentence().isLast(),
                                this.sentence().content() + other.sentence().content()
                        )
                ))
                .orElse(this);
    }

}
