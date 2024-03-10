package io.github.oldmanpushcart.qianfan4j.pluginapp;

import io.github.oldmanpushcart.internal.qianfan4j.pluginapp.PluginAppRequestBuilderImpl;
import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoRequest;
import io.github.oldmanpushcart.qianfan4j.base.api.Option;
import io.github.oldmanpushcart.qianfan4j.chat.message.Message;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 插件应用请求
 */
public interface PluginAppRequest extends AlgoRequest<PluginAppModel, PluginAppResponse> {

    /**
     * 获取提问
     *
     * @return 提问
     */
    String question();

    /**
     * 获取文件URI
     *
     * @return 文件URI
     */
    URI fileUri();

    /**
     * 获取插件列表
     *
     * @return 插件列表
     */
    Set<Plugin> plugins();

    /**
     * 获取变量列表
     * <p>如果prompt中使用了变量，推理时可以填写具体值</p>
     *
     * @return 变量列表
     */
    Map<String, Object> variables();

    /**
     * 获取聊天上下文信息
     *
     * @return 聊天上下文信息
     */
    List<Message> history();

    /**
     * 获取LLM参数
     *
     * @return LLM参数
     */
    Option llm();

    /**
     * 构建插件应用请求
     *
     * @return 插件应用请求构建器
     */
    static Builder newBuilder() {
        return new PluginAppRequestBuilderImpl();
    }

    /**
     * 插件应用请求构建器
     */
    interface Builder extends AlgoRequest.Builder<PluginAppModel, PluginAppRequest, Builder> {

        /**
         * 设置提问
         *
         * @param question 提问
         * @return this
         */
        Builder question(String question);

        /**
         * 设置文件URI
         *
         * @param fileUri 文件URI
         * @return this
         */
        Builder fileUri(URI fileUri);

        /**
         * 添加插件
         *
         * @param plugins 插件
         * @return this
         */
        Builder plugins(Plugin... plugins);

        /**
         * 添加变量
         *
         * @param variables 变量
         * @return this
         */
        Builder variables(Map<String, Object> variables);

        /**
         * 添加变量
         *
         * @param name  变量名
         * @param value 变量值
         * @return this
         */
        Builder variables(String name, Object value);

        /**
         * 添加聊天上下文信息
         *
         * @param messages 聊天上下文信息
         * @return this
         */
        Builder history(List<Message> messages);

        /**
         * 添加聊天上下文信息
         *
         * @param messages 聊天上下文信息
         * @return this
         */
        Builder history(Message... messages);

        /**
         * 设置LLM参数
         *
         * @param llm LLM参数
         * @return this
         */
        Builder llm(Option llm);

        /**
         * 设置LLM参数
         *
         * @param name  参数名
         * @param value 参数值
         * @return this
         */
        Builder llm(String name, Object value);

    }

}
