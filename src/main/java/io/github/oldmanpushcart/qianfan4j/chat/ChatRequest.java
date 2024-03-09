package io.github.oldmanpushcart.qianfan4j.chat;

import io.github.oldmanpushcart.internal.qianfan4j.chat.ChatRequestBuilderImpl;
import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoRequest;
import io.github.oldmanpushcart.qianfan4j.chat.function.ChatFunction;
import io.github.oldmanpushcart.qianfan4j.chat.message.Message;

import java.util.List;

public interface ChatRequest extends AlgoRequest<ChatModel, ChatResponse> {

    List<Message> messages();

    List<ChatFunction<?, ?>> functions();

    static Builder newBuilder() {
        return new ChatRequestBuilderImpl();
    }

    static Builder newBuilder(ChatRequest request) {
        return new ChatRequestBuilderImpl(request);
    }

    interface Builder extends AlgoRequest.Builder<ChatModel, ChatRequest, Builder> {

        Builder messages(Message... messages);

        Builder messages(List<Message> messages);

        Builder functions(ChatFunction<?, ?>... functions);

    }

}
