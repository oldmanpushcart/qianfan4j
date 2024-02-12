package io.github.ompc.erniebot4j.pluginapp;

import io.github.ompc.erniebot4j.chat.ChatOptions;
import io.github.ompc.erniebot4j.executor.Option;

/**
 * 插件应用选项
 */
public interface PluginAppOptions extends ChatOptions {

    /**
     * 是否返回原始信息
     */
    Option.SimpleOpt<Boolean> IS_VERBOSE = new Option.SimpleOpt<>("verbose", Boolean.class);

}
