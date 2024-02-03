package io.github.ompc.erniebot4j.image.caption.http;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.ompc.erniebot4j.image.caption.CaptionImageRequest;
import io.github.ompc.erniebot4j.util.ImageUtils;

import java.io.IOException;
import java.util.HashMap;

import static io.github.ompc.erniebot4j.util.StringUtils.isNotBlank;
import static java.util.Objects.nonNull;

public class CaptionImageRequestJsonSerializer extends JsonSerializer<CaptionImageRequest> {

    @Override
    public void serialize(CaptionImageRequest request, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeObject(new HashMap<>() {{

            put("prompt", request.prompt());
            put("image", ImageUtils.imageToBase64(request.image()));

            // 用户
            if (isNotBlank(request.user())) {
                put("user_id", request.user());
            }

            // 设置选项
            if (nonNull(request.options()) && !request.options().isEmpty()) {
                putAll(request.options().dump());
            }

        }});
    }


}
