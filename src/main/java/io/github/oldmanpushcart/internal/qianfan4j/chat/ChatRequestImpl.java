package io.github.oldmanpushcart.internal.qianfan4j.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoRequestImpl;
import io.github.oldmanpushcart.qianfan4j.base.api.Option;
import io.github.oldmanpushcart.qianfan4j.chat.ChatModel;
import io.github.oldmanpushcart.qianfan4j.chat.ChatRequest;
import io.github.oldmanpushcart.qianfan4j.chat.ChatResponse;
import io.github.oldmanpushcart.qianfan4j.chat.function.ChatFunction;
import io.github.oldmanpushcart.qianfan4j.chat.message.Message;

import java.time.Duration;
import java.util.List;

public class ChatRequestImpl extends AlgoRequestImpl<ChatModel, ChatResponse> implements ChatRequest {

    @JsonProperty("messages")
    private final List<Message> messages;

    @JsonSerialize(contentUsing = ChatFunctionJsonSerializer.class)
    @JsonProperty("functions")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ChatFunction<?, ?>> functions;

    private final String _string;

    protected ChatRequestImpl(Duration timeout, ChatModel model, Option option, String user, List<Message> messages, List<ChatFunction<?, ?>> functions) {
        super(timeout, model, option, user, ChatResponseImpl.class);
        this.messages = messages;
        this.functions = functions;
        this._string = "qianfan://chat/%s".formatted(model.name());
    }

    @Override
    public String toString() {
        return _string;
    }

    @Override
    public List<Message> messages() {
        return messages;
    }

    @Override
    public List<ChatFunction<?, ?>> functions() {
        return functions;
    }

}
