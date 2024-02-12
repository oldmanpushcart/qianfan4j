package io.github.ompc.erniebot4j.pluginapp;

import io.github.ompc.erniebot4j.chat.message.Message;
import io.github.ompc.erniebot4j.executor.BaseRequest;

import java.net.URL;
import java.util.*;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;
import static java.util.Objects.requireNonNull;

/**
 * 插件应用请求
 */
public final class PluginAppRequest extends BaseRequest<PluginAppModel> {

    private final String question;
    private final URL fileUrl;
    private final Set<Plugin> plugins;
    private final Map<String, Object> variables;
    private final List<Message> messages;

    private PluginAppRequest(Builder builder) {
        super(builder);
        this.question = requireNonNull(builder.question);
        this.plugins = check(builder.plugins, v -> !v.isEmpty(), "plugins is empty");
        this.variables = builder.variables;
        this.messages = builder.messages;
        this.fileUrl = builder.fileUrl;
    }

    /**
     * 获取问题
     *
     * @return 问题
     */
    public String question() {
        return question;
    }

    /**
     * 获取插件列表
     *
     * @return 插件列表
     */
    public Set<Plugin> plugins() {
        return plugins;
    }

    /**
     * 获取变量
     *
     * @return 变量
     */
    public Map<String, Object> variables() {
        return variables;
    }

    /**
     * 获取对话历史
     *
     * @return 对话历史
     */
    public List<Message> messages() {
        return messages;
    }

    /**
     * 文件URL
     *
     * @return 文件URL
     */
    public URL fileUrl() {
        return fileUrl;
    }

    /**
     * 插件请求构建器
     */
    public static class Builder extends BaseBuilder<PluginAppModel, PluginAppRequest, Builder> {

        private String question;
        private URL fileUrl;
        private final Set<Plugin> plugins = new HashSet<>();
        private final Map<String, Object> variables = new HashMap<>();
        private final List<Message> messages = new ArrayList<>();

        /**
         * 设置问题
         *
         * @param question 问题
         * @return this
         */
        public Builder question(String question) {
            this.question = requireNonNull(question);
            return this;
        }

        /**
         * 设置文件URL
         *
         * @param fileUrl 文件URL
         * @return this
         */
        public Builder fileUrl(URL fileUrl) {
            this.fileUrl = requireNonNull(fileUrl);
            return this;
        }

        /**
         * 设置插件集合。
         * <p>
         * 指明本次插件请求所需要使用的插件，自带插件参见{@link Plugin}。
         * <ul>
         *     <li>注意：插件应用需要配置开启对应的插件</li>
         *     <li>如需要更多插件，可以访问插件表<a href="https://cloud.baidu.com/doc/WENXINWORKSHOP/s/llmuflu2o">插件列表</a></li>
         *     <li>如需要自定义插件，可以访问<a href="https://cloud.baidu.com/doc/WENXINWORKSHOP/s/2lmuqfng8">插件开发者文档</a></li>
         * </ul>
         * </p>
         *
         * @param plugins 插件集合
         * @return this
         */
        public Builder plugins(Set<Plugin> plugins) {
            this.plugins.addAll(plugins);
            return this;
        }

        /**
         * @see #plugins(Set)
         */
        public Builder plugins(Plugin... plugins) {
            this.plugins.addAll(Set.of(plugins));
            return this;
        }

        /**
         * 设置变量表
         * <p>
         * 在千帆中配置插件应用时，你会有机会给这个插件应用配置prompt，
         * 如果prompt中使用了变量，推理时可以在此填写变量具体值。
         * </p>
         *
         * @param variables 变量表
         * @return this
         */
        public Builder variables(Map<String, Object> variables) {
            this.variables.putAll(variables);
            return this;
        }

        /**
         * 设置变量
         *
         * @param name  变量名
         * @param value 变量值
         * @return this
         * @see #variables(Map)
         */
        public Builder variable(String name, Object value) {
            this.variables.put(name, value);
            return this;
        }

        /**
         * 设置对话历史
         * <p>
         * 用于推理时，上下文的对话历史。
         * </p>
         *
         * @param messages 对话历史
         * @return this
         */
        public Builder messages(List<Message> messages) {
            this.messages.addAll(messages);
            return this;
        }

        /**
         * @see #messages(List)
         */
        public Builder messages(Message... messages) {
            return messages(List.of(messages));
        }

        /**
         * 构建插件请求
         *
         * @return 插件请求
         */
        @Override
        public PluginAppRequest build() {
            return new PluginAppRequest(this);
        }

    }

}
