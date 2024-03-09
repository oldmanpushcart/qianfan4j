package io.github.oldmanpushcart.internal.qianfan4j.chat;

import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoRequestBuilderImpl;
import io.github.oldmanpushcart.qianfan4j.chat.ChatModel;
import io.github.oldmanpushcart.qianfan4j.chat.ChatRequest;
import io.github.oldmanpushcart.qianfan4j.chat.function.ChatFunction;
import io.github.oldmanpushcart.qianfan4j.chat.message.Message;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class ChatRequestBuilderImpl extends AlgoRequestBuilderImpl<ChatModel, ChatRequest, ChatRequest.Builder>
        implements ChatRequest.Builder {

    private List<Message> messages = new ArrayList<>();
    private List<ChatFunction<?,?>> functions = new ArrayList<>();

    public ChatRequestBuilderImpl() {

    }

    public ChatRequestBuilderImpl(ChatRequest request) {
        super(request);
        this.messages = request.messages();
        this.functions = request.functions();
    }

    @Override
    public ChatRequest.Builder messages(Message... messages) {
        this.messages.addAll(List.of(messages));
        return this;
    }

    @Override
    public ChatRequest.Builder messages(List<Message> messages) {
        this.messages.addAll(messages);
        return this;
    }

    @Override
    public ChatRequest.Builder functions(ChatFunction<?, ?>... functions) {
        this.functions.addAll(List.of(functions));
        return this;
    }

    @Override
    public ChatRequest build() {
        return new ChatRequestImpl(
                timeout(),
                requireNonNull(model()),
                option(),
                user(),
                messages,
                functions
        );
    }

}
