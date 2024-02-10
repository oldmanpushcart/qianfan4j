package io.github.ompc.erniebot4j.plugin;

import io.github.ompc.erniebot4j.chat.ChatOptions;
import io.github.ompc.erniebot4j.executor.Option;

public interface PluginOptions extends ChatOptions {

    /**
     * 是否返回原始信息
     */
    Option.SimpleOpt<Boolean> IS_VERBOSE = new Option.SimpleOpt<>("verbose", Boolean.class);

}
