package io.github.ompc.erniebot4j.plugin.knowledgebase;

import io.github.ompc.erniebot4j.chat.message.Message;
import io.github.ompc.erniebot4j.executor.BaseRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class KnowledgeBaseRequest extends BaseRequest<KnowledgeModel> {

    private final String query;
    private final Map<String, Object> variables;
    private final List<Message> messages;

    protected KnowledgeBaseRequest(Builder builder) {
        super(builder);
        this.query = requireNonNull(builder.query);
        this.variables = builder.variables;
        this.messages = builder.messages;
    }

    public String query() {
        return query;
    }

    public Map<String, Object> variables() {
        return variables;
    }

    public List<Message> messages() {
        return messages;
    }

    public static class Builder extends BaseBuilder<KnowledgeModel, KnowledgeBaseRequest, Builder> {

        private String query;
        private final Map<String, Object> variables = new HashMap<>();
        private final List<Message> messages = new ArrayList<>();

        public Builder query(String query) {
            this.query = requireNonNull(query);
            return this;
        }

        public Builder variables(Map<String, Object> variables) {
            this.variables.putAll(variables);
            return this;
        }

        public Builder variable(String name, Object value) {
            this.variables.put(name, value);
            return this;
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

        @Override
        public KnowledgeBaseRequest build() {
            return new KnowledgeBaseRequest(this);
        }

    }

}
