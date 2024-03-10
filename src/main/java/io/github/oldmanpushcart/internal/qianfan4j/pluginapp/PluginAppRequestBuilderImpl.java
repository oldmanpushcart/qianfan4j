package io.github.oldmanpushcart.internal.qianfan4j.pluginapp;

import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoRequestBuilderImpl;
import io.github.oldmanpushcart.qianfan4j.base.api.Option;
import io.github.oldmanpushcart.qianfan4j.chat.message.Message;
import io.github.oldmanpushcart.qianfan4j.pluginapp.Plugin;
import io.github.oldmanpushcart.qianfan4j.pluginapp.PluginAppModel;
import io.github.oldmanpushcart.qianfan4j.pluginapp.PluginAppRequest;

import java.net.URI;
import java.util.*;

public class PluginAppRequestBuilderImpl extends AlgoRequestBuilderImpl<PluginAppModel, PluginAppRequest, PluginAppRequest.Builder>
        implements PluginAppRequest.Builder {

    private String question;
    private URI fileUri;
    private final Set<Plugin> plugins = new HashSet<>();
    private final Map<String, Object> variables = new HashMap<>();
    private final List<Message> history = new ArrayList<>();
    private final Option llm = new Option();

    @Override
    public PluginAppRequest.Builder question(String question) {
        this.question = Objects.requireNonNull(question);
        return this;
    }

    @Override
    public PluginAppRequest.Builder fileUri(URI fileUri) {
        this.fileUri = Objects.requireNonNull(fileUri);
        return this;
    }

    @Override
    public PluginAppRequest.Builder plugins(Plugin... plugins) {
        this.plugins.addAll(List.of(plugins));
        return this;
    }

    @Override
    public PluginAppRequest.Builder variables(Map<String, Object> variables) {
        this.variables.putAll(variables);
        return this;
    }

    @Override
    public PluginAppRequest.Builder variables(String name, Object value) {
        this.variables.put(name, value);
        return this;
    }

    @Override
    public PluginAppRequest.Builder history(List<Message> messages) {
        this.history.addAll(messages);
        return this;
    }

    @Override
    public PluginAppRequest.Builder history(Message... messages) {
        this.history.addAll(List.of(messages));
        return this;
    }

    @Override
    public PluginAppRequest.Builder llm(Option llm) {
        llm.export().forEach(llm::option);
        return this;
    }

    @Override
    public PluginAppRequest.Builder llm(String name, Object value) {
        this.llm.option(name, value);
        return this;
    }

    @Override
    public PluginAppRequest build() {
        return new PluginAppRequestImpl(
                timeout(),
                model(),
                option(),
                user(),
                question,
                fileUri,
                plugins,
                variables,
                history,
                llm
        );
    }

}
