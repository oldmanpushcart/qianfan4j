package io.github.oldmanpushcart.internal.qianfan4j.image.caption;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.oldmanpushcart.internal.qianfan4j.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageJsonSerializer extends JsonSerializer<BufferedImage> {

    @Override
    public void serialize(BufferedImage image, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeString(ImageUtils.imageToBase64(image));
    }

}
