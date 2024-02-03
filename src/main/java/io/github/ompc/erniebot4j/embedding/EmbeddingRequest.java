package io.github.ompc.erniebot4j.embedding;

import io.github.ompc.erniebot4j.executor.Model;
import io.github.ompc.erniebot4j.executor.Option;
import io.github.ompc.erniebot4j.executor.Request;

import java.time.Duration;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElseGet;

public class EmbeddingRequest implements Request {

    private final EmbeddingModel model;
    private final String user;
    private final Option options;
    private final Duration timeout;
    private final List<String> documents;

    private EmbeddingRequest(Builder builder) {
        this.model = requireNonNull(builder.model);
        this.documents = requireNonNull(builder.documents);
        this.options = requireNonNullElseGet(builder.options, Option::new);
        this.user = builder.user;
        this.timeout = builder.timeout;
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

    public List<String> documents() {
        return documents;
    }

    public <T, R> EmbeddingRequest option(Option.Opt<T, R> opt, T value) {
        options.option(opt, value);
        return this;
    }

    public static class Builder {

        private EmbeddingModel model;
        private String user;
        private Option options;
        private Duration timeout;
        private List<String> documents;

        public Builder model(EmbeddingModel model) {
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

        public Builder documents(List<String> documents) {
            this.documents = documents;
            return this;
        }

        public Builder documents(String... documents) {
            this.documents = List.of(documents);
            return this;
        }

        public EmbeddingRequest build() {
            return new EmbeddingRequest(this);
        }

    }

}
