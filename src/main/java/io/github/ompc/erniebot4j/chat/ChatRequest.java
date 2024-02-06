package io.github.ompc.erniebot4j.chat;

import io.github.ompc.erniebot4j.chat.function.ChatFunction;
import io.github.ompc.erniebot4j.chat.function.ChatFunctionKit;
import io.github.ompc.erniebot4j.chat.message.Message;
import io.github.ompc.erniebot4j.executor.BaseRequest;
import io.github.ompc.erniebot4j.executor.Model;
import io.github.ompc.erniebot4j.executor.Option;
import io.github.ompc.erniebot4j.executor.Request;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;
import static java.util.Objects.requireNonNull;

/**
 * 对话请求
 */
public final class ChatRequest extends BaseRequest implements Request {

    private final List<Message> messages;
    private final ChatFunctionKit kit;

    private ChatRequest(Builder builder) {
        super(
                requireNonNull(builder.model),
                builder.option,
                builder.timeout,
                builder.user
        );
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
    public static class Builder {
        private Model model;
        private String user;
        private List<Message> messages = new ArrayList<>();
        private ChatFunctionKit kit = new ChatFunctionKit();
        private Option option = new Option();
        private Duration timeout;

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
            this.model = request.model();
            this.option = request.option();
            this.timeout = request.timeout();
            this.user = request.user();
            this.messages = request.messages;
            this.kit = request.kit;
        }

        public Builder model(ChatModel model) {
            this.model = requireNonNull(model);
            return this;
        }

        public Builder user(String user) {
            this.user = requireNonNull(user);
            return this;
        }

        public Builder messages(List<Message> messages) {
            this.messages.addAll(messages);
            return this;
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

        public Builder option(Option options) {
            this.option.load(options);
            return this;
        }

        public <T, R> Builder option(Option.Opt<T, R> opt, T value) {
            option.option(opt, value);
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = requireNonNull(timeout);
            return this;
        }

        public ChatRequest build() {
            return new ChatRequest(this);
        }

    }

}
