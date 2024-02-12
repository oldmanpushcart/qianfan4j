package io.github.ompc.erniebot4j.pluginapp.http;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.ompc.erniebot4j.pluginapp.Plugin;
import io.github.ompc.erniebot4j.pluginapp.PluginAppRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

class PluginAppRequestJsonSerializer extends JsonSerializer<PluginAppRequest> {

    @Override
    public void serialize(PluginAppRequest request, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeObject(new HashMap<>() {{

            // 问题
            put("query", request.question());

            // 文件
            if (Objects.nonNull(request.fileUrl())) {
                put("fileurl", request.fileUrl());
            }

            // 插件
            if (!request.plugins().isEmpty()) {
                put("plugins", request.plugins().stream()
                        .map(Plugin::id)
                        .toArray(String[]::new)
                );
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
