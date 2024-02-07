package io.github.ompc.erniebot4j.image.caption;

import io.github.ompc.erniebot4j.executor.Aggregatable;
import io.github.ompc.erniebot4j.executor.Response;
import io.github.ompc.erniebot4j.executor.Sentence;
import io.github.ompc.erniebot4j.executor.Usage;

import java.util.Optional;

public record CaptionImageResponse(
        String id,
        String type,
        long timestamp,
        Usage usage,
        Sentence sentence
) implements Response, Aggregatable<CaptionImageResponse> {

    @Override
    public CaptionImageResponse aggregate(CaptionImageResponse response) {
        return Optional.ofNullable(response)
                .map(other -> new CaptionImageResponse(
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
