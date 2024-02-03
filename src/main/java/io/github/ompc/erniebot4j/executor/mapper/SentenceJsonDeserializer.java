package io.github.ompc.erniebot4j.executor.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.github.ompc.erniebot4j.executor.Sentence;

import java.io.IOException;

public class SentenceJsonDeserializer extends JsonDeserializer<Sentence> {

    @Override
    public Sentence deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        final var node = context.readTree(parser);
        return !node.has("sentence_id") ?
                Sentence.last(node.get("result").asText()) :
                new Sentence(
                        node.get("sentence_id").asInt(),
                        node.get("is_end").asBoolean(),
                        node.get("result").asText()
                );
    }

}
