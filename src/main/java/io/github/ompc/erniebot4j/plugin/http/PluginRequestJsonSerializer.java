package io.github.ompc.erniebot4j.plugin.http;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.ompc.erniebot4j.plugin.Plugin;
import io.github.ompc.erniebot4j.plugin.PluginRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class PluginRequestJsonSerializer extends JsonSerializer<PluginRequest> {

    @Override
    public void serialize(PluginRequest request, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeObject(new HashMap<>() {{
            // 问题
            put("query", request.question());

            // 插件
            if (!request.plugins().isEmpty()) {
                put("plugins", request.plugins().stream()
                        .map(Plugin::text)
                        .toArray(String[]::new)
                );
            }

            // 图片
            if (Objects.nonNull(request.imageUrl())) {
                put("fileurl", request.imageUrl());
            }

            // 变量
            if (!request.variables().isEmpty()) {
                put("input_variables", request.variables());
            }

            // 对话历史
            if (!request.messages().isEmpty()) {
                put("history", request.messages());
            }

            // 选项参数
            final var option = request.option().copy();
            if (option.has("stream")) {
                put("stream", option.remove("stream"));
            }

            // 是否返回RAW信息
            if (option.has("verbose")) {
                put("verbose", option.remove("verbose"));
            }

            // LLM参数
            if (!option.isEmpty()) {
                put("llm", option.export());
            }
        }});
    }

}
