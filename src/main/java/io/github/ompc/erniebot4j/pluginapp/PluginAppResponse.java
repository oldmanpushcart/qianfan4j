package io.github.ompc.erniebot4j.pluginapp;

import io.github.ompc.erniebot4j.executor.Aggregatable;
import io.github.ompc.erniebot4j.executor.Response;
import io.github.ompc.erniebot4j.executor.Sentence;
import io.github.ompc.erniebot4j.executor.Usage;

import java.util.Optional;

/**
 * 插件应用应答
 *
 * @param id        应答ID
 * @param type      应答类型
 * @param timestamp 应答时间戳
 * @param usage     应答用量
 * @param logId     日志ID
 * @param sentence  应答句子
 * @param meta      应答元数据，只有设置了{@link PluginAppOptions#IS_VERBOSE}才会有值
 */
public record PluginAppResponse(
        String id,
        String type,
        long timestamp,
        Usage usage,
        long logId,
        Sentence sentence,
        Meta meta
) implements Response, Aggregatable<PluginAppResponse> {

    @Override
    public PluginAppResponse aggregate(PluginAppResponse response) {
        return Optional.ofNullable(response)
                .map(other -> new PluginAppResponse(
                        this.id(),
                        this.type(),
                        this.timestamp(),
                        other.usage(),
                        this.logId(),
                        new Sentence(
                                this.sentence().index(),
                                this.sentence().isLast() || other.sentence().isLast(),
                                this.sentence().content() + other.sentence().content()
                        ),
                        this.meta()
                ))
                .orElse(this);
    }

    /**
     * 插件应答元数据
     * <p>
     * 在这里可以查看插件调用的相关信息
     * </p>
     *
     * @param pluginId 插件ID
     * @param raw      原始请求和响应
     */
    public record Meta(String pluginId, Raw raw) {

    }

    /**
     * 插件原始请求和响应
     *
     * @param request  原始请求
     * @param response 原始响应
     */
    public record Raw(String request, String response) {

    }

}
