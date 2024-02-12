package io.github.ompc.erniebot4j.image.generation.http;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.ompc.erniebot4j.executor.Usage;
import io.github.ompc.erniebot4j.image.generation.GenImageResponse;
import io.github.ompc.erniebot4j.util.CheckUtils;
import io.github.ompc.erniebot4j.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.stream.IntStream;

class GenImageResponseJsonDeserializer extends JsonDeserializer<GenImageResponse> {

    @Override
    public GenImageResponse deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        final var node = context.readTree(parser);
        return new GenImageResponse(
                node.get("id").asText(),
                node.get("object").asText(),
                deserializeTimestamp(node),
                deserializeUsage(node, context),
                deserializeImages(node)
        );
    }

    private long deserializeTimestamp(JsonNode node) {
        return node.get("created").asInt() * 1000L;
    }

    private BufferedImage[] deserializeImages(JsonNode node) {
        if (!node.has("data")) {
            return new BufferedImage[0];
        }
        final var imagesNode = node.get("data");
        return IntStream.range(0, imagesNode.size())
                .mapToObj(i -> deserializeImage(imagesNode.get(i)))
                .toArray(BufferedImage[]::new);
    }

    private BufferedImage deserializeImage(JsonNode node) {

        final var type = node.get("object").asText();
        CheckUtils.check(type, type.equals("image"), "invalid type: " + type);

        final var b64 = node.get("b64_image").asText();
        try {
            return ImageUtils.base64ToImage(b64);
        } catch (IOException cause) {
            throw new RuntimeException("decode image base64 error!", cause);
        }

    }

    private Usage deserializeUsage(JsonNode node, DeserializationContext context) throws IOException {
        return node.has("usage")
                ? context.readValue(node.get("usage").traverse(), Usage.class)
                : new Usage();
    }

}
