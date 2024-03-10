package io.github.oldmanpushcart.internal.qianfan4j.image.generation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.github.oldmanpushcart.internal.qianfan4j.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageJsonDeserializer extends JsonDeserializer<BufferedImage> {

    @Override
    public BufferedImage deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        final var node = context.readTree(parser);
        return ImageUtils.base64ToImage(node.get("b64_image").asText());
    }

}
