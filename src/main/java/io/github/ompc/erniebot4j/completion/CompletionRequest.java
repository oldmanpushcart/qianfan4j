package io.github.ompc.erniebot4j.completion;

import io.github.ompc.erniebot4j.chat.ChatRequest;
import io.github.ompc.erniebot4j.executor.Model;
import io.github.ompc.erniebot4j.executor.Option;
import io.github.ompc.erniebot4j.executor.Request;

import java.awt.image.BufferedImage;
import java.time.Duration;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElseGet;

public class CompletionRequest implements Request {

    private final Model model;
    private final String user;
    private final Option options;
    private final Duration timeout;
    private final String prompt;

    private CompletionRequest(Builder builder) {
        this.model = requireNonNull(builder.model);
        this.options = requireNonNullElseGet(builder.options, Option::new);
        this.prompt = requireNonNull(builder.prompt);
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

    public String prompt() {
        return prompt;
    }

    public <T, R> CompletionRequest option(Option.Opt<T, R> opt, T value) {
        options.option(opt, value);
        return this;
    }

    public static class Builder {
        private Model model;
        private String user;
        private Option options;
        private Duration timeout;
        private String prompt;
        private BufferedImage image;

        public Builder model(Model model) {
            this.model = model;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
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

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public CompletionRequest build() {
            return new CompletionRequest(this);
        }

    }

}
