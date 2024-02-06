package io.github.ompc.erniebot4j.chat.http;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.ompc.erniebot4j.chat.ChatRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;

import static io.github.ompc.erniebot4j.util.StringUtils.isNotBlank;
import static java.util.Objects.nonNull;

public class ChatRequestJsonSerializer extends JsonSerializer<ChatRequest> {

    @Override
    public void serialize(ChatRequest request, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeObject(new HashMap<>() {{

            // 对话消息
            put("messages", request.messages());

            // 函数列表
            if (nonNull(request.kit()) && !request.kit().isEmpty()) {
                put("functions", request.kit().stream()
                        .map(stub -> stub.meta().schema())
                        .collect(Collectors.toList())
                );
            }

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
