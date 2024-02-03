package io.github.ompc.erniebot4j.completion;

import io.github.ompc.erniebot4j.executor.Response;
import io.github.ompc.erniebot4j.executor.Sentence;
import io.github.ompc.erniebot4j.executor.Usage;

public record CompletionResponse(
        String id,
        String type,
        long timestamp,
        Usage usage,
        Sentence sentence
) implements Response {

}
