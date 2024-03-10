package io.github.oldmanpushcart.internal.qianfan4j.chat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoResponseImpl;
import io.github.oldmanpushcart.qianfan4j.base.algo.Usage;
import io.github.oldmanpushcart.qianfan4j.base.api.Ret;
import io.github.oldmanpushcart.qianfan4j.chat.ChatResponse;
import io.github.oldmanpushcart.qianfan4j.chat.FunctionCall;
import io.github.oldmanpushcart.qianfan4j.chat.Search;

import java.io.IOException;
import java.util.*;

public class ChatResponseImpl extends AlgoResponseImpl implements ChatResponse {

    private final boolean isLast;
    private final boolean isSafe;
    private final String content;
    private final Search search;
    private final FunctionCall functionCall;

    private ChatResponseImpl(String uuid, Ret ret, Usage usage, boolean isLast, boolean isSafe, String content, Search search, FunctionCall functionCall) {
        super(uuid, ret, usage);
        this.isLast = isLast;
        this.isSafe = isSafe;
        this.content = content;
        this.search = search;
        this.functionCall = functionCall;
    }

    @Override
    public boolean isLast() {
        return isLast;
    }

    @Override
    public String content() {
        return content;
    }

    @Override
    public boolean isSafe() {
        return isSafe;
    }

    @Override
    public boolean isFunctionCall() {
        return Objects.nonNull(functionCall);
    }

    @Override
    public Search search() {
        return search;
    }

    public FunctionCall call() {
        return functionCall;
    }

    @JsonCreator
    static ChatResponseImpl of(

            @JsonProperty("id")
            String uuid,

            @JsonProperty("error_code")
            String code,

            @JsonProperty("error_msg")
            String msg,

            @JsonProperty(value = "is_end", defaultValue = "true")
            boolean isLast,

            @JsonProperty(value = "need_clear_history", defaultValue = "false")
            boolean isNotSafe,

            @JsonProperty("result")
            String content,

            @JsonProperty("usage")
            Usage usage,

            @JsonProperty("function_call")
            FunctionCall functionCall,

            @JsonDeserialize(using = SearchJsonDeserializer.class)
            @JsonProperty("search_info")
            Search search

    ) {
        final var ret = Ret.of(code, msg);
        final var isSafe = !isNotSafe;
        return new ChatResponseImpl(uuid, ret, usage, isLast, isSafe, content, search, functionCall);
    }

    @Override
    public ChatResponse aggregate(ChatResponse response) {
        return Optional.ofNullable(response)
                .<ChatResponse>map(other -> new ChatResponseImpl(
                        other.uuid(),
                        other.ret(),
                        other.usage(),
                        other.isLast(),
                        other.isSafe(),
                        this.content() + other.content(),
                        other.search(),
                        other.call()
                ))
                .orElse(this);
    }

    private static class SearchJsonDeserializer extends JsonDeserializer<Search> {

        @Override
        public Search deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            final var node = context.readTree(parser);
            final var items = new ArrayList<Search.Item>();
            final var searchResultsNode = node.get("search_results");
            if (Objects.nonNull(searchResultsNode) && !searchResultsNode.isNull()) {
                items.addAll(List.of(context.readTreeAsValue(searchResultsNode, Search.Item[].class)));

            }
            return new Search(Collections.unmodifiableList(items));
        }

    }

}
