package io.github.ompc.erniebot4j.plugin.knowledgebase.http;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.ompc.erniebot4j.executor.Sentence;
import io.github.ompc.erniebot4j.executor.Usage;
import io.github.ompc.erniebot4j.plugin.Plugin;
import io.github.ompc.erniebot4j.plugin.knowledgebase.KnowledgeBaseResponse;

import java.io.IOException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class KnowledgeBaseResponseJsonDeserializer extends JsonDeserializer<KnowledgeBaseResponse> {

    @Override
    public KnowledgeBaseResponse deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        final var node = context.readTree(parser);
        return new KnowledgeBaseResponse(
                node.get("id").asText(),
                node.get("object").asText(),
                deserializeTimestamp(node),
                deserializeUsage(node, context),
                node.get("log_id").asLong(),
                deserializeSentence(node, context),
                deserializeMeta(node, context)
        );
    }

    private long deserializeTimestamp(JsonNode node) {
        return node.get("created").asInt() * 1000L;
    }

    private Sentence deserializeSentence(JsonNode node, DeserializationContext context) throws IOException {
        return node.has("sentence_id")
                ? context.readValue(node.traverse(), Sentence.class)
                : Sentence.last(node.get("result").asText());
    }

    private Usage deserializeUsage(JsonNode node, DeserializationContext context) throws IOException {
        return node.has("usage")
                ? context.readValue(node.get("usage").traverse(), Usage.class)
                : new Usage();
    }

    private KnowledgeBaseResponse.Meta deserializeMeta(JsonNode node, DeserializationContext context) throws IOException {
        return node.has("meta_info")
                ? context.readValue(node.get("meta_info").traverse(), KnowledgeBaseResponse.Meta.class)
                : null;
    }

}
