package io.github.ompc.erniebot4j.plugin.knowledgebase;

import io.github.ompc.erniebot4j.executor.Aggregatable;
import io.github.ompc.erniebot4j.executor.Response;
import io.github.ompc.erniebot4j.executor.Sentence;
import io.github.ompc.erniebot4j.executor.Usage;
import io.github.ompc.erniebot4j.plugin.Plugin;

import java.util.Optional;

public record KnowledgeBaseResponse(
        String id,
        String type,
        long timestamp,
        Usage usage,
        long logId,
        Sentence sentence,
        Meta meta
) implements Response, Aggregatable<KnowledgeBaseResponse> {

    @Override
    public KnowledgeBaseResponse aggregate(KnowledgeBaseResponse response) {
        return Optional.ofNullable(response)
                .map(other -> new KnowledgeBaseResponse(
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

    public record Meta(Plugin plugin, RawRequest request, RawResponse response) {

    }

    public record RawRequest(String query, String[] kbs, float score, int topN) {

    }

    public record RawResponse(int code, String message, RawCost cost, RawDocument[] documents) {

    }

    public record RawCost(long bes, long db, long embedded, long signed) {

    }

    public record RawDocument(
            String download,
            String id,
            String name,
            String kbId,
            float score,
            Shard shard,
            String content
    ) {

    }

    public record Shard(String id, int index) {

    }

}
