package io.github.ompc.erniebot4j.plugin;

import io.github.ompc.erniebot4j.executor.Textualizable;

public enum Plugin implements Textualizable {

    /**
     * 知识库
     */
    KNOWLEDGE_BASE("uuid-zhishiku");

    private final String text;

    Plugin(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

}
