package io.github.ompc.erniebot4j.plugin.knowledgebase;

import io.github.ompc.erniebot4j.chat.ChatOptions;
import io.github.ompc.erniebot4j.executor.Option.SimpleOpt;
import io.github.ompc.erniebot4j.executor.Options;

public interface KnowledgeBaseOptions extends ChatOptions {

    /**
     * 是否返回原始信息
     */
    SimpleOpt<Boolean> IS_VERBOSE = new SimpleOpt<>("verbose", Boolean.class);

}
