package io.github.oldmanpushcart.internal.qianfan4j.pluginapp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoResponseImpl;
import io.github.oldmanpushcart.qianfan4j.base.algo.Usage;
import io.github.oldmanpushcart.qianfan4j.base.api.Ret;
import io.github.oldmanpushcart.qianfan4j.pluginapp.PluginAppResponse;

import java.util.Objects;
import java.util.Optional;

import static io.github.oldmanpushcart.internal.qianfan4j.util.StringUtils.contact;

public class PluginAppResponseImpl extends AlgoResponseImpl implements PluginAppResponse {

    private final boolean isLast;
    private final boolean isSafe;
    private final String content;
    private final Meta meta;

    PluginAppResponseImpl(String uuid, Ret ret, Usage usage, boolean isLast, boolean isSafe, String content, Meta meta) {
        super(uuid, ret, usage);
        this.isLast = isLast;
        this.isSafe = isSafe;
        this.content = content;
        this.meta = meta;
    }

    @Override
    public boolean isLast() {
        return isLast;
    }

    @Override
    public boolean isSafe() {
        return isSafe;
    }

    @Override
    public String content() {
        return content;
    }

    @Override
    public Meta meta() {
        return meta;
    }

    @Override
    public PluginAppResponse aggregate(PluginAppResponse response) {
        return Optional.ofNullable(response)
                .<PluginAppResponse>map(other -> new PluginAppResponseImpl(
                        other.uuid(),
                        other.ret(),
                        other.usage(),
                        other.isLast(),
                        other.isSafe(),
                        contact(this.content(), other.content()),
                        Optional.ofNullable(meta()).orElse(response.meta())
                ))
                .orElse(this);
    }

    @JsonCreator
    static PluginAppResponseImpl of(

            @JsonProperty("log_id")
            String uuid,

            @JsonProperty("error_code")
            String code,

            @JsonProperty("error_msg")
            String msg,

            @JsonProperty("usage")
            Usage usage,

            @JsonProperty(value = "is_end", defaultValue = "true")
            boolean isLast,

            @JsonProperty(value = "need_clear_history", defaultValue = "false")
            boolean isNotSafe,

            @JsonProperty("result")
            String content,

            @JsonProperty("meta_info")
            Meta meta

    ) {
        return new PluginAppResponseImpl(
                uuid,
                Ret.of(code, msg),
                Objects.isNull(usage) ? Usage.ofEmpty() : usage,
                isLast,
                !isNotSafe,
                content,
                meta
        );
    }

}
