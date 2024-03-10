package io.github.oldmanpushcart.internal.qianfan4j.image.generation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoRequestImpl;
import io.github.oldmanpushcart.internal.qianfan4j.util.JacksonUtils;
import io.github.oldmanpushcart.qianfan4j.base.api.Option;
import io.github.oldmanpushcart.qianfan4j.image.generation.GenerationImageModel;
import io.github.oldmanpushcart.qianfan4j.image.generation.GenerationImageRequest;
import io.github.oldmanpushcart.qianfan4j.image.generation.GenerationImageResponse;

import java.time.Duration;

public class GenerationImageRequestImpl extends AlgoRequestImpl<GenerationImageModel, GenerationImageResponse>
        implements GenerationImageRequest {

    private static final ObjectMapper mapper = JacksonUtils.mapper();

    @JsonProperty("prompt")
    private final String prompt;

    @JsonProperty("negative_prompt")
    private final String negative;

    GenerationImageRequestImpl(Duration timeout, GenerationImageModel model, Option option, String user, String prompt, String negative) {
        super(timeout, model, option, user, GenerationImageResponseImpl.class);
        this.prompt = prompt;
        this.negative = negative;
    }

    @Override
    public String prompt() {
        return prompt;
    }

    @Override
    public String negative() {
        return negative;
    }

    @Override
    protected String wrapLoggingResponseBody(String body) {
        String content;
        try {
            final var node = JacksonUtils.toNode(mapper, body);
            node.get("data").forEach(image -> {
                final var imageNode = (ObjectNode) image;
                final var size = imageNode.get("b64_image").asText().length();
                imageNode.put("b64_image", "...(base64, size: %d bytes)".formatted(size));
            });
            content = node.toString();
        } catch (Exception cause) {
            // ignore
            content = body;
        }
        return content;
    }

}
