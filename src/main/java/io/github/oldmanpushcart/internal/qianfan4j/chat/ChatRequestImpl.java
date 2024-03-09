package io.github.oldmanpushcart.internal.qianfan4j.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoRequestImpl;
import io.github.oldmanpushcart.qianfan4j.base.api.Option;
import io.github.oldmanpushcart.qianfan4j.chat.ChatModel;
import io.github.oldmanpushcart.qianfan4j.chat.ChatRequest;
import io.github.oldmanpushcart.qianfan4j.chat.ChatResponse;
import io.github.oldmanpushcart.qianfan4j.chat.function.ChatFunctionKit;
import io.github.oldmanpushcart.qianfan4j.chat.message.Message;

import java.time.Duration;
import java.util.List;

public class ChatRequestImpl extends AlgoRequestImpl<ChatModel, ChatResponse> implements ChatRequest {

    @JsonProperty("messages")
    private final List<Message> messages;

    @JsonSerialize(using = ChatFunctionKitJsonSerializer.class)
    @JsonProperty("functions")
    private final ChatFunctionKit kit;

    private final String _string;

    protected ChatRequestImpl(Duration timeout, ChatModel model, Option option, String user, List<Message> messages, ChatFunctionKit kit) {
        super(timeout, model, option, user, ChatResponseImpl.class);
        this.messages = messages;
        this.kit = kit;
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
    public ChatFunctionKit kit() {
        return kit;
    }

}
