package io.github.ompc.erniebot4j.embedding;

import io.github.ompc.erniebot4j.executor.BaseRequest;
import io.github.ompc.erniebot4j.executor.Option;
import io.github.ompc.erniebot4j.executor.Request;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;
import static java.util.Objects.requireNonNull;

public class EmbeddingRequest extends BaseRequest implements Request {

    private final List<String> documents;

    private EmbeddingRequest(Builder builder) {
        super(
                requireNonNull(builder.model),
                builder.option,
                builder.timeout,
                builder.user
        );
        this.documents = check(builder.documents, v -> null != v && !v.isEmpty(), "documents is empty");
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
            return documents(List.of(documents));
        }

        public EmbeddingRequest build() {
            return new EmbeddingRequest(this);
        }

    }

}
