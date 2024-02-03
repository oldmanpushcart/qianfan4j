package io.github.ompc.erniebot4j.image.caption.http;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.ompc.erniebot4j.executor.Sentence;
import io.github.ompc.erniebot4j.executor.Usage;
import io.github.ompc.erniebot4j.image.caption.CaptionImageResponse;

import java.io.IOException;

public class CaptionImageResponseJsonDeserializer extends JsonDeserializer<CaptionImageResponse> {

    @Override
    public CaptionImageResponse deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        final var node = context.readTree(parser);
        return new CaptionImageResponse(
                node.get("id").asText(),
                node.get("object").asText(),
                deserializeTimestamp(node),
                deserializeUsage(node, context),
                deserializeSentence(node, context)
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

}
