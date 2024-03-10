package io.github.oldmanpushcart.qianfan4j.pluginapp;

import io.github.oldmanpushcart.qianfan4j.base.api.Option;
import io.github.oldmanpushcart.qianfan4j.chat.ChatOptions;

/**
 * 插件应用选项
 */
public interface PluginAppOptions extends ChatOptions {

    /**
     * 是否返回原始信息
     */
    Option.SimpleOpt<Boolean> IS_VERBOSE = new Option.SimpleOpt<>("verbose", Boolean.class);

}
