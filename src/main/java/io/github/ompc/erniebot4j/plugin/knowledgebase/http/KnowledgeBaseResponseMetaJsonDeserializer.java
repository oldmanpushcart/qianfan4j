package io.github.ompc.erniebot4j.plugin.knowledgebase.http;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.ompc.erniebot4j.plugin.Plugin;
import io.github.ompc.erniebot4j.plugin.knowledgebase.KnowledgeBaseResponse;

import java.io.IOException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class KnowledgeBaseResponseMetaJsonDeserializer extends JsonDeserializer<KnowledgeBaseResponse.Meta> {

    @Override
    public KnowledgeBaseResponse.Meta deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return deserializeMeta(context.readTree(parser));
    }

    private KnowledgeBaseResponse.Meta deserializeMeta(JsonNode node) {
        return new KnowledgeBaseResponse.Meta(
                deserializePlugin(node),
                deserializeRawRequest(node.get("request")),
                deserializeRawResponse(node.get("response"))
        );
    }

    private Plugin deserializePlugin(JsonNode node) {
        final var text = node.get("plugin_id").asText();
        return Stream.of(Plugin.values())
                .filter(plugin -> plugin.getText().equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("unknown plugin: %s".formatted(text)));
    }

    private KnowledgeBaseResponse.RawRequest deserializeRawRequest(JsonNode node) {
        return new KnowledgeBaseResponse.RawRequest(
                node.get("query").asText(),
                StreamSupport.stream(node.get("kbIds").spliterator(), false)
                        .map(JsonNode::asText)
                        .toArray(String[]::new),
                node.get("score").floatValue(),
                node.get("topN").intValue()
        );
    }

    private KnowledgeBaseResponse.RawResponse deserializeRawResponse(JsonNode node) {
        final var resultNode = node.get("result");
        return new KnowledgeBaseResponse.RawResponse(
                node.get("retCode").intValue(),
                node.get("message").asText(),
                new KnowledgeBaseResponse.RawCost(
                        resultNode.get("besQueryCostMilsec3").asInt(),
                        resultNode.get("dbQueryCostMilsec1").asInt(),
                        resultNode.get("embeddedCostMilsec2").asInt(),
                        resultNode.get("urlSignedCostMilsec4").asInt()
                ),
                deserializeRawDocuments(resultNode.get("responses"))
        );
    }

    private KnowledgeBaseResponse.RawDocument[] deserializeRawDocuments(JsonNode node) {
        return StreamSupport.stream(node.spliterator(), false)
                .map(this::deserializeRawDocument)
                .toArray(KnowledgeBaseResponse.RawDocument[]::new);
    }

    private KnowledgeBaseResponse.RawDocument deserializeRawDocument(JsonNode node) {
        return new KnowledgeBaseResponse.RawDocument(
                node.get("contentUrl").asText(),
                node.get("docId").asText(),
                node.get("docName").asText(),
                node.get("kbId").asText(),
                node.get("score").floatValue(),
                new KnowledgeBaseResponse.Shard(
                        node.get("shardId").asText(),
                        node.get("shardIndex").intValue()
                ),
                node.get("content").asText()
        );
    }

}
