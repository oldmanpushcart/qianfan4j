package io.github.oldmanpushcart.internal.qianfan4j.pluginapp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoRequestImpl;
import io.github.oldmanpushcart.internal.qianfan4j.util.JacksonUtils;
import io.github.oldmanpushcart.qianfan4j.base.api.Option;
import io.github.oldmanpushcart.qianfan4j.chat.message.Message;
import io.github.oldmanpushcart.qianfan4j.pluginapp.Plugin;
import io.github.oldmanpushcart.qianfan4j.pluginapp.PluginAppModel;
import io.github.oldmanpushcart.qianfan4j.pluginapp.PluginAppRequest;
import io.github.oldmanpushcart.qianfan4j.pluginapp.PluginAppResponse;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class PluginAppRequestImpl extends AlgoRequestImpl<PluginAppModel, PluginAppResponse> implements PluginAppRequest {

    private static final ObjectMapper mapper = JacksonUtils.mapper();

    @JsonProperty("query")
    private final String question;

    @JsonProperty("fileurl")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final URI fileUri;

    @JsonProperty("plugins")
    private final Set<Plugin> plugins;

    @JsonProperty("input_variables")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final Map<String, Object> variables;

    @JsonProperty("history")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<Message> history;

    @JsonIgnore
    private final Option llm;

    @JsonProperty("llm")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    Map<String, Object> exportLlm() {
        return llm.export();
    }

    private final String _string;

    PluginAppRequestImpl(Duration timeout, PluginAppModel model, Option option, String user, String question, URI fileUri, Set<Plugin> plugins, Map<String, Object> variables, List<Message> history, Option llm) {
        super(timeout, model, option, user, PluginAppResponseImpl.class);
        this.question = question;
        this.fileUri = fileUri;
        this.plugins = plugins;
        this.variables = variables;
        this.history = history;
        this.llm = llm;
        this._string = "qianfan://plugin-app/%s".formatted(model.name());
    }

    @Override
    public String toString() {
        return _string;
    }

    @Override
    public Function<String, PluginAppResponse> responseDeserializer() {
        return json -> {
            final var node = JacksonUtils.toNode(mapper, json);
            final var isFirstSSE = node.has("plugin_id");
            if (!isFirstSSE) {
                return super.responseDeserializer().apply(json);
            }
            // 处理SSE首包，为META-INFO信息
            final var meta = JacksonUtils.toObject(mapper, PluginAppResponse.Meta.class, node);
            return new PluginAppResponseImpl(
                    null,
                    null,
                    null,
                    false,
                    true,
                    null,
                    meta
            );
        };
    }


    @Override
    public String question() {
        return question;
    }

    @Override
    public URI fileUri() {
        return fileUri;
    }

    @Override
    public Set<Plugin> plugins() {
        return plugins;
    }

    @Override
    public Map<String, Object> variables() {
        return variables;
    }

    @Override
    public List<Message> history() {
        return history;
    }

    @Override
    public Option llm() {
        return llm;
    }

}
