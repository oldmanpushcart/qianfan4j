package io.github.oldmanpushcart.qianfan4j.pluginapp;

import io.github.oldmanpushcart.qianfan4j.base.algo.Model;

/**
 * 插件应用模型
 * <p>
 * 插件应用是一个特殊的模型，它的本质是一个LLM远程服务背后挂着文心一言的插件。
 * 你可以到千帆大模型平台<a href="https://console.bce.baidu.com/qianfan/plugin/service/list">插件编排</a>完成插件应用的创建。
 * 模型的远程地址由{@code PREFIX+APP-ID}决定，{@code app-id}是插件应用的唯一标识。
 * </p>
 *
 * @param appId 插件应用的唯一标识
 */
public record PluginAppModel(String appId) implements Model {

    private static final String prefix = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/plugin";

    @Override
    public String name() {
        return appId();
    }

    @Override
    public String remote() {
        return "%s/%s/".formatted(prefix, appId());
    }

}