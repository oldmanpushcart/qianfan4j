package io.github.ompc.erniebot4j.image.generation.http;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.ompc.erniebot4j.image.generation.GenImageRequest;

import java.io.IOException;
import java.util.HashMap;

import static io.github.ompc.erniebot4j.util.StringUtils.isNotBlank;
import static java.util.Objects.nonNull;

public class GenImageRequestJsonSerializer extends JsonSerializer<GenImageRequest> {

    @Override
    public void serialize(GenImageRequest request, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeObject(new HashMap<>() {{

            put("prompt", request.prompt());

            // 负向提示
            if(isNotBlank(request.negative())) {
                put("negative_prompt", request.negative());
            }

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
