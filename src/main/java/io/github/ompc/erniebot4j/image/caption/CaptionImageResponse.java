package io.github.ompc.erniebot4j.image.caption;

import io.github.ompc.erniebot4j.executor.Response;
import io.github.ompc.erniebot4j.executor.Sentence;
import io.github.ompc.erniebot4j.executor.Usage;

public record CaptionImageResponse(
        String id,
        String type,
        long timestamp,
        Usage usage,
        Sentence sentence
) implements Response {
    @Override
    public String id() {
        return id;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public long timestamp() {
        return timestamp;
    }

    @Override
    public Usage usage() {
        return usage;
    }


}
