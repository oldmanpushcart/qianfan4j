package io.github.ompc.erniebot4j.plugin;

import io.github.ompc.erniebot4j.chat.message.Message;
import io.github.ompc.erniebot4j.executor.BaseRequest;

import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;
import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElseGet;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class PluginRequest extends BaseRequest<PluginModel> {

    private final String question;
    private final Supplier<CompletableFuture<URL>> imageUrlSupplier;
    private final Set<Plugin> plugins;
    private final Map<String, Object> variables;
    private final List<Message> messages;

    protected PluginRequest(Builder builder) {
        super(builder);
        this.question = requireNonNull(builder.question);
        this.plugins = check(builder.plugins, v -> !v.isEmpty(), "plugins is empty");
        this.variables = builder.variables;
        this.messages = builder.messages;
        this.imageUrlSupplier = requireNonNullElseGet(builder.imageUrlSupplier, () -> () -> completedFuture(null));
    }

    public String question() {
        return question;
    }

    public Set<Plugin> plugins() {
        return plugins;
    }

    public Map<String, Object> variables() {
        return variables;
    }

    public List<Message> messages() {
        return messages;
    }

    public CompletableFuture<URL> fetchImageUrl() {
        return imageUrlSupplier.get();
    }

    public static class Builder extends BaseBuilder<PluginModel, PluginRequest, Builder> {

        private String question;
        private Supplier<CompletableFuture<URL>> imageUrlSupplier;
        private final Set<Plugin> plugins = new HashSet<>();
        private final Map<String, Object> variables = new HashMap<>();
        private final List<Message> messages = new ArrayList<>();

        public Builder question(String question) {
            this.question = requireNonNull(question);
            return this;
        }

        public Builder imageUrl(Supplier<CompletableFuture<URL>> supplier) {
            this.imageUrlSupplier = requireNonNull(supplier);
            return this;
        }

        public Builder plugins(Set<Plugin> plugins) {
            this.plugins.addAll(plugins);
            return this;
        }

        public Builder plugins(Plugin... plugins) {
            this.plugins.addAll(Set.of(plugins));
            return this;
        }

        public Builder plugin(Plugin plugin) {
            this.plugins.add(plugin);
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
        public PluginRequest build() {
            return new PluginRequest(this);
        }

    }

}
