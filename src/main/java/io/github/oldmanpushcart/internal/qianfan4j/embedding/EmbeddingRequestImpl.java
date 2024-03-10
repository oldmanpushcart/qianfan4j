package io.github.oldmanpushcart.internal.qianfan4j.embedding;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoRequestImpl;
import io.github.oldmanpushcart.internal.qianfan4j.util.JacksonUtils;
import io.github.oldmanpushcart.qianfan4j.base.api.Option;
import io.github.oldmanpushcart.qianfan4j.embedding.EmbeddingModel;
import io.github.oldmanpushcart.qianfan4j.embedding.EmbeddingRequest;
import io.github.oldmanpushcart.qianfan4j.embedding.EmbeddingResponse;

import java.time.Duration;
import java.util.List;

public class EmbeddingRequestImpl extends AlgoRequestImpl<EmbeddingModel, EmbeddingResponse> implements EmbeddingRequest {

    private static final ObjectMapper mapper = JacksonUtils.mapper();

    @JsonProperty("input")
    private final List<String> texts;

    private final String _string;

    EmbeddingRequestImpl(Duration timeout, EmbeddingModel model, Option option, String user, List<String> texts) {
        super(timeout, model, option, user, EmbeddingResponseImpl.class);
        this.texts = texts;
        this._string = "qianfan://embedding/%s".formatted(model.name());
    }

    @Override
    public String toString() {
        return _string;
    }

    @Override
    public List<String> texts() {
        return texts;
    }

    @Override
    protected String wrapLoggingRequestBody(String body) {
        String content;
        try {
            final var node = JacksonUtils.toNode(mapper, body);
            final var newInputNode = new ArrayNode(mapper.getNodeFactory());
            node.get("input").forEach(item -> {
                final var length = item.asText().length();
                newInputNode.add("...(text, length: %d)".formatted(length));
            });
            ((ObjectNode)node).set("input", newInputNode);
            content = node.toString();
        } catch (Exception cause) {
            // ignore
            content = body;
        }
        return content;
    }

    @Override
    protected String wrapLoggingResponseBody(String body) {
        String content;
        try {
            final var node = JacksonUtils.toNode(mapper, body);
            node.get("data").forEach(embedding -> {
                final var embeddingNode = (ObjectNode) embedding;
                final var size = embeddingNode.get("embedding").size() * Float.BYTES;
                embeddingNode.put("embedding", "...(embedding, size: %d bytes)".formatted(size));
            });
            content = node.toString();
        } catch (Exception cause) {
            // ignore
            content = body;
        }
        return content;
    }

}
