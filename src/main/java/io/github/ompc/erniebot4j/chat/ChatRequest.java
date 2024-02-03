package io.github.ompc.erniebot4j.chat;

import io.github.ompc.erniebot4j.chat.function.ChatFunction;
import io.github.ompc.erniebot4j.chat.function.ChatFunctionKit;
import io.github.ompc.erniebot4j.chat.message.Message;
import io.github.ompc.erniebot4j.executor.Model;
import io.github.ompc.erniebot4j.executor.Option;
import io.github.ompc.erniebot4j.executor.Request;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElseGet;

public final class ChatRequest implements Request {
    private final ChatModel model;
    private final Option options;
    private final Duration timeout;
    private final String user;
    private final List<Message> messages;
    private final ChatFunctionKit kit;

    private ChatRequest(Builder builder) {
        this.model = requireNonNull(builder.model);
        this.messages = requireNonNullElseGet(builder.messages, ArrayList::new);
        this.kit = requireNonNullElseGet(builder.kit, ChatFunctionKit::new);
        this.options = requireNonNullElseGet(builder.options, Option::new);
        this.timeout = builder.timeout;
        this.user = builder.user;
    }

    @Override
    public Model model() {
        return model;
    }

    @Override
    public String user() {
        return user;
    }

    @Override
    public Option options() {
        return options;
    }

    @Override
    public Duration timeout() {
        return timeout;
    }

    public List<Message> messages() {
        return messages;
    }

    public ChatRequest message(Message message) {
        messages.add(message);
        return this;
    }

    public <T, R> ChatRequest option(Option.Opt<T, R> opt, T value) {
        options.option(opt, value);
        return this;
    }

    public ChatFunctionKit kit() {
        return kit;
    }

    public ChatRequest function(ChatFunction<?, ?> function) {
        kit.load(function);
        return this;
    }

    public static class Builder {
        private ChatModel model;
        private String user;
        private List<Message> messages;
        private ChatFunctionKit kit;
        private Option options;
        private Duration timeout;

        public Builder reference(ChatRequest request) {
            this.model = request.model;
            this.user = request.user;
            this.messages = request.messages;
            this.kit = request.kit;
            this.options = request.options;
            this.timeout = request.timeout;
            return this;
        }

        public Builder model(ChatModel model) {
            this.model = model;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder messages(List<Message> messages) {
            this.messages = messages;
            return this;
        }

        public Builder kit(ChatFunctionKit kit) {
            this.kit = kit;
            return this;
        }

        public Builder options(Option options) {
            this.options = options;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public ChatRequest build() {
            return new ChatRequest(this);
        }

    }

}
