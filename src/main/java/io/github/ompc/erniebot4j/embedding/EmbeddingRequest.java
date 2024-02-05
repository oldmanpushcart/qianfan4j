package io.github.ompc.erniebot4j.embedding;

import io.github.ompc.erniebot4j.executor.Option;
import io.github.ompc.erniebot4j.executor.Request;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;
import static java.util.Objects.requireNonNull;

public class EmbeddingRequest implements Request {

    private final EmbeddingModel model;
    private final String user;
    private final Option option;
    private final Duration timeout;
    private final List<String> documents;

    private EmbeddingRequest(Builder builder) {
        this.model = requireNonNull(builder.model);
        this.documents = check(builder.documents, v -> null != v && !v.isEmpty(), "documents is empty");
        this.option = builder.option;
        this.user = builder.user;
        this.timeout = builder.timeout;
    }

    @Override
    public EmbeddingModel model() {
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

    public List<String> documents() {
        return documents;
    }

    public static class Builder {

        private EmbeddingModel model;
        private String user;
        private final Option option = new Option();
        private Duration timeout;
        private final List<String> documents = new ArrayList<>();

        public Builder model(EmbeddingModel model) {
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

        public Builder documents(List<String> documents) {
            this.documents.addAll(documents);
            return this;
        }

        public Builder documents(String... documents) {
            this.documents.addAll(List.of(documents));
            return this;
        }

        public EmbeddingRequest build() {
            return new EmbeddingRequest(this);
        }

    }

}
