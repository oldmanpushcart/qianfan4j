package io.github.ompc.erniebot4j.chat.http;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.ompc.erniebot4j.chat.ChatResponse;
import io.github.ompc.erniebot4j.chat.ChatResponse.Search;
import io.github.ompc.erniebot4j.executor.Sentence;
import io.github.ompc.erniebot4j.executor.Usage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;

public class ChatResponseJsonDeserializer extends JsonDeserializer<ChatResponse> {

    @Override
    public ChatResponse deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        final var node = context.readTree(parser);
        return new ChatResponse(
                node.get("id").asText(),
                node.get("object").asText(),
                deserializeTimestamp(node),
                deserializeUsage(node, context),
                deserializeSentence(node, context),
                deserializeFunctionCall(node),
                deserializeSearch(node)
        );
    }

    private long deserializeTimestamp(JsonNode node) {
        return node.get("created").asInt() * 1000L;
    }

    private Search deserializeSearch(JsonNode node) {
        if (!node.has("search_info")) {
            return new Search(emptyList());
        }
        final var items = new ArrayList<Search.Item>();
        final var searchInfoNode = node.get("search_info");
        final var resultsNode = searchInfoNode.get("search_results");
        IntStream.range(0, resultsNode.size())
                .mapToObj(resultsNode::get)
                .map(resultNode -> new Search.Item(
                        resultNode.get("url").asText(),
                        resultNode.get("title").asText()
                ))
                .forEachOrdered(items::add);
        return new Search(items);
    }

    private ChatResponse.FunctionCall deserializeFunctionCall(JsonNode node) {
        if (!node.has("function_call")) {
            return null;
        }
        final var fcNode = node.get("function_call");
        return new ChatResponse.FunctionCall(
                fcNode.get("name").asText(),
                fcNode.has("thoughts") ? fcNode.get("thoughts").asText() : null,
                fcNode.get("arguments").asText()
        );
    }

    private Sentence deserializeSentence(JsonNode node, DeserializationContext context) throws IOException {
        return node.has("sentence_id")
                ? context.readValue(node.traverse(), Sentence.class)
                : Sentence.last(node.get("result").asText());
    }

    private Usage deserializeUsage(JsonNode node, DeserializationContext context) throws IOException {
        return node.has("usage")
                ? context.readValue(node.get("usage").traverse(), Usage.class)
                : new Usage();
    }

}
