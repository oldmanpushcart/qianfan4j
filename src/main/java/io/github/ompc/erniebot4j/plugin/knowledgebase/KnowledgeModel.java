package io.github.ompc.erniebot4j.plugin.knowledgebase;

import io.github.ompc.erniebot4j.executor.Model;

public record KnowledgeModel(String endpoint) implements Model {

    private static final String prefix = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/plugin";

    @Override
    public String name() {
        return endpoint();
    }

    @Override
    public String remote() {
        return "%s/%s/".formatted(prefix, endpoint());
    }

}
