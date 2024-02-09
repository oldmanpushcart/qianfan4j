package io.github.ompc.erniebot4j.chat;

import io.github.ompc.erniebot4j.chat.function.ChatFunction;
import io.github.ompc.erniebot4j.chat.function.ChatFunctionKit;
import io.github.ompc.erniebot4j.chat.message.Message;
import io.github.ompc.erniebot4j.executor.BaseRequest;
import io.github.ompc.erniebot4j.executor.Request;

import java.util.ArrayList;
import java.util.List;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;
import static java.util.Objects.requireNonNull;

/**
 * 对话请求
 */
public final class ChatRequest extends BaseRequest<ChatModel> implements Request {

    private final List<Message> messages;
    private final ChatFunctionKit kit;

    private ChatRequest(Builder builder) {
        super(builder);
        this.messages = check(builder.messages, v -> null != v && !v.isEmpty(), "messages is empty");
        this.kit = builder.kit;
    }

    /**
     * 获取对话内容
     *
     * @return 对话内容
     */
    public List<Message> messages() {
        return messages;
    }

    /**
     * 获取函数库
     *
     * @return 函数库
     */
    public ChatFunctionKit kit() {
        return kit;
    }

    /**
     * 对话请求构建器
     */
    public static class Builder extends BaseBuilder<ChatModel, ChatRequest, Builder> {

        private List<Message> messages = new ArrayList<>();
        private ChatFunctionKit kit = new ChatFunctionKit();

        /**
         * 对话请求构建器
         */
        public Builder() {

        }

        /**
         * 对话请求构建器；从已有的对话请求构建
         *
         * @param request 已有的对话请求
         */
        public Builder(ChatRequest request) {
            super(request);
            this.messages = request.messages;
            this.kit = request.kit;
        }

        public Builder messages(List<Message> messages) {
            this.messages.addAll(messages);
            return this;
        }

        public Builder messages(Message... messages) {
            return messages(List.of(messages));
        }

        public Builder message(Message message) {
            this.messages.add(message);
            return this;
        }

        public Builder replaceKit(ChatFunctionKit kit) {
            this.kit = requireNonNull(kit);
            return this;
        }

        public Builder kit(ChatFunctionKit kit) {
            this.kit.load(kit);
            return this;
        }

        public Builder function(ChatFunction<?, ?> function) {
            this.kit.load(function);
            return this;
        }

        @Override
        public ChatRequest build() {
            return new ChatRequest(this);
        }

    }

}
