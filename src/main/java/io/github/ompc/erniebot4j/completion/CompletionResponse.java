package io.github.ompc.erniebot4j.completion;

import io.github.ompc.erniebot4j.executor.Mergeable;
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
) implements Response, Mergeable<CompletionResponse> {

    @Override
    public CompletionResponse merge(CompletionResponse response) {
        return Optional.ofNullable(response)
                .map(other -> new CompletionResponse(
                        this.id(),
                        this.type(),
                        this.timestamp(),
                        other.usage(),
                        new Sentence(
                                this.sentence().index(),
                                this.sentence().isLast() || other.sentence().isLast(),
                                this.sentence().content() + other.sentence().content()
                        )
                ))
                .orElse(this);
    }

}
