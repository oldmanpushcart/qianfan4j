package io.github.ompc.erniebot4j.plugin.http;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.ompc.erniebot4j.executor.Sentence;
import io.github.ompc.erniebot4j.executor.Usage;
import io.github.ompc.erniebot4j.plugin.PluginResponse;

import java.io.IOException;
import java.util.UUID;

import static io.github.ompc.erniebot4j.util.JacksonUtils.getTextDefault;

public class PluginResponseJsonDeserializer extends JsonDeserializer<PluginResponse> {


    @Override
    public PluginResponse deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        final var node = context.readTree(parser);
        return new PluginResponse(
                deserializeId(node),
                deserializeType(node),
                deserializeTimestamp(node),
                deserializeUsage(node, context),
                node.get("log_id").asLong(),
                deserializeSentence(node, context),
                deserializeMeta(node)
        );
    }

    private String deserializeId(JsonNode node) {
        return node.has("id")
                ? node.get("id").asText()
                : "FAKE-ID-%s".formatted(UUID.randomUUID().toString());
    }

    private String deserializeType(JsonNode node) {
        return node.has("object")
                ? node.get("object").asText()
                : "FAKE-TYPE";
    }

    private long deserializeTimestamp(JsonNode node) {
        return node.has("created")
                ? node.get("created").asInt() * 1000L
                : System.currentTimeMillis();
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

    private PluginResponse.Meta deserializeMeta(JsonNode node) {
        if (!node.has("meta_info")) {
            return null;
        }
        final var metaNode = node.get("meta_info");
        return new PluginResponse.Meta(
                getTextDefault(metaNode, "plugin_id", null),
                new PluginResponse.Raw(
                        metaNode.get("request").toString(),
                        metaNode.get("response").toString()
                )
        );
    }

}
