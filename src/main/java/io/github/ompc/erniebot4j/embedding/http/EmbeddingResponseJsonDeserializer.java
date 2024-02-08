package io.github.ompc.erniebot4j.embedding.http;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.ompc.erniebot4j.embedding.Embedding;
import io.github.ompc.erniebot4j.embedding.EmbeddingResponse;
import io.github.ompc.erniebot4j.executor.Usage;
import io.github.ompc.erniebot4j.util.CheckUtils;

import java.io.IOException;
import java.util.stream.StreamSupport;

public class EmbeddingResponseJsonDeserializer extends JsonDeserializer<EmbeddingResponse> {

    @Override
    public EmbeddingResponse deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        final var node = context.readTree(parser);
        return new EmbeddingResponse(
                node.get("id").asText(),
                node.get("object").asText(),
                deserializeTimestamp(node),
                deserializeUsage(node, context),
                deserializeEmbeddings(node)
        );
    }

    private long deserializeTimestamp(JsonNode node) {
        return node.get("created").asInt() * 1000L;
    }

    private Embedding[] deserializeEmbeddings(JsonNode node) {
        if (!node.has("data")) {
            return new Embedding[0];
        }
        final var embeddingsNode = node.get("data");
        return StreamSupport.stream(embeddingsNode.spliterator(), false)
                .map(this::deserializeEmbedding)
                .sorted()
                .toArray(Embedding[]::new);
    }

    private Embedding deserializeEmbedding(JsonNode node) {
        final var type = node.get("object").asText();
        CheckUtils.check(type, type.equals("embedding"), "invalid type: " + type);
        final var vectorsNode = node.get("embedding");
        final var vectors = new float[vectorsNode.size()];
        for (int i = 0; i < vectorsNode.size(); i++) {
            vectors[i] = vectorsNode.get(i).floatValue();
        }
        return new Embedding(
                node.get("index").asInt(),
                vectors
        );
    }

    private Usage deserializeUsage(JsonNode node, DeserializationContext context) throws IOException {
        return node.has("usage")
                ? context.readValue(node.get("usage").traverse(), Usage.class)
                : new Usage();
    }

}
