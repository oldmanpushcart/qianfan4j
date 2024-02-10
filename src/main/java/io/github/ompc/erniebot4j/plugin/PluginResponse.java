package io.github.ompc.erniebot4j.plugin;

import io.github.ompc.erniebot4j.executor.Aggregatable;
import io.github.ompc.erniebot4j.executor.Response;
import io.github.ompc.erniebot4j.executor.Sentence;
import io.github.ompc.erniebot4j.executor.Usage;

import java.util.Optional;

public record PluginResponse(
        String id,
        String type,
        long timestamp,
        Usage usage,
        long logId,
        Sentence sentence,
        Meta meta
) implements Response, Aggregatable<PluginResponse> {

    @Override
    public PluginResponse aggregate(PluginResponse response) {
        return Optional.ofNullable(response)
                .map(other -> new PluginResponse(
                        this.id(),
                        this.type(),
                        this.timestamp(),
                        other.usage(),
                        this.logId(),
                        new Sentence(
                                this.sentence().index(),
                                this.sentence().isLast() || other.sentence().isLast(),
                                this.sentence().content() + other.sentence().content()
                        ),
                        this.meta()
                ))
                .orElse(this);
    }

    public record Meta(String pluginId, Raw raw) {

    }

    public record Raw(String request, String response) {

    }

}
