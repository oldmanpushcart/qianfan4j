package io.github.ompc.erniebot4j.completion;

import io.github.ompc.erniebot4j.executor.BaseRequest;
import io.github.ompc.erniebot4j.executor.Option;
import io.github.ompc.erniebot4j.executor.Request;

import java.time.Duration;

import static java.util.Objects.requireNonNull;

public class CompletionRequest extends BaseRequest implements Request {

    private final String prompt;

    private CompletionRequest(Builder builder) {
        super(
                requireNonNull(builder.model),
                builder.option,
                builder.timeout,
                builder.user
        );
        this.prompt = requireNonNull(builder.prompt);
    }

    public String prompt() {
        return prompt;
    }

    public static class Builder {
        private CompletionModel model;
        private String user;
        private final Option option = new Option();
        private Duration timeout;
        private String prompt;

        public Builder model(CompletionModel model) {
            this.model = requireNonNull(model);
            return this;
        }

        public Builder user(String user) {
            this.user = requireNonNull(user);
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

        public Builder prompt(String prompt) {
            this.prompt = requireNonNull(prompt);
            return this;
        }

        public CompletionRequest build() {
            return new CompletionRequest(this);
        }

    }

}
