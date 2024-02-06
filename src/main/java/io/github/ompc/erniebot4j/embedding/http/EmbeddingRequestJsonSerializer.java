package io.github.ompc.erniebot4j.embedding.http;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.ompc.erniebot4j.embedding.EmbeddingRequest;

import java.io.IOException;
import java.util.HashMap;

import static io.github.ompc.erniebot4j.util.StringUtils.isNotBlank;
import static java.util.Objects.nonNull;

public class EmbeddingRequestJsonSerializer extends JsonSerializer<EmbeddingRequest> {

    @Override
    public void serialize(EmbeddingRequest request, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeObject(new HashMap<>() {{

            // 文档
            put("input", request.documents());

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
