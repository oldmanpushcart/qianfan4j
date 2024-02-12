package io.github.ompc.erniebot4j.completion.http;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.ompc.erniebot4j.completion.CompletionRequest;

import java.io.IOException;
import java.util.HashMap;

import static io.github.ompc.erniebot4j.util.StringUtils.isNotBlank;
import static java.util.Objects.nonNull;

class CompletionRequestJsonSerializer extends JsonSerializer<CompletionRequest> {

    @Override
    public void serialize(CompletionRequest request, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeObject(new HashMap<>() {{

            put("prompt", request.prompt());

            // 用户
            if (isNotBlank(request.user())) {
                put("user_id", request.user());
            }

            // 设置选项
            if (nonNull(request.option()) && !request.option().isEmpty()) {
                putAll(request.option().export());
            }

        }});
    }


}
