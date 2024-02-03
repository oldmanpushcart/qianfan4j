package io.github.ompc.erniebot4j.executor.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.github.ompc.erniebot4j.executor.Usage;

import java.io.IOException;

import static io.github.ompc.erniebot4j.util.JacksonUtils.getIntDefault;

public class UsageJsonDeserializer extends JsonDeserializer<Usage> {

    @Override
    public Usage deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        final var node = context.readTree(parser);
        return new Usage(
                getIntDefault(node, "prompt_tokens", 0),
                getIntDefault(node, "completion_tokens", 0),
                getIntDefault(node, "total_tokens", 0)
        );
    }

}
