package io.github.ompc.erniebot4j.chat;

import io.github.ompc.erniebot4j.chat.function.ChatFunction;
import io.github.ompc.erniebot4j.chat.function.ChatFunctionKit;
import io.github.ompc.erniebot4j.chat.message.Message;
import io.github.ompc.erniebot4j.executor.Option;
import io.github.ompc.erniebot4j.executor.Request;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;
import static java.util.Objects.requireNonNull;

public final class ChatRequest implements Request {
    private final ChatModel model;
    private final Option option;
    private final Duration timeout;
    private final String user;
    private final List<Message> messages;
    private final ChatFunctionKit kit;

    private ChatRequest(Builder builder) {
        this.model = requireNonNull(builder.model);
        this.messages = check(builder.messages, v -> null != v && !v.isEmpty(), "messages is empty");
        this.kit = builder.kit;
        this.option = builder.option;
        this.timeout = builder.timeout;
        this.user = builder.user;
    }

    @Override
    public ChatModel model() {
        return model;
    }

    @Override
    public String user() {
        return user;
    }

    @Override
    public Option option() {
        return option;
    }

    @Override
    public Duration timeout() {
        return timeout;
    }

    public List<Message> messages() {
        return messages;
    }

    public ChatFunctionKit kit() {
        return kit;
    }

    public static class Builder {
        private ChatModel model;
        private String user;
        private List<Message> messages = new ArrayList<>();
        private ChatFunctionKit kit = new ChatFunctionKit();
        private Option option = new Option();
        private Duration timeout;

        public Builder() {

        }

        public Builder(ChatRequest request) {
            this.model = request.model;
            this.user = request.user;
            this.messages = request.messages;
            this.kit = request.kit;
            this.option = request.option;
            this.timeout = request.timeout;
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
