package io.github.ompc.erniebot4j.plugin.knowledgebase.http;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.ompc.erniebot4j.plugin.Plugin;
import io.github.ompc.erniebot4j.plugin.knowledgebase.KnowledgeBaseRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class KnowledgeBaseRequestJsonSerializer extends JsonSerializer<KnowledgeBaseRequest> {

    @Override
    public void serialize(KnowledgeBaseRequest request, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeObject(new HashMap<>() {{
            put("query", request.query());
            put("plugins", List.of(Plugin.KNOWLEDGE_BASE));

            if (!request.variables().isEmpty()) {
                put("input_variables", request.variables());
            }

            if (!request.messages().isEmpty()) {
                put("history", request.messages());
            }

            final var option = request.option().copy();
            if (option.has("stream")) {
                put("stream", option.remove("stream"));
            }

            if (option.has("verbose")) {
                put("verbose", option.remove("verbose"));
            }

            if (!option.isEmpty()) {
                put("llm", option.export());
            }

        }});
    }

}
