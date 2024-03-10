package io.github.oldmanpushcart.internal.qianfan4j.image.caption;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoResponseImpl;
import io.github.oldmanpushcart.qianfan4j.base.algo.Usage;
import io.github.oldmanpushcart.qianfan4j.base.api.Ret;
import io.github.oldmanpushcart.qianfan4j.image.caption.CaptionImageResponse;

import java.util.Optional;

public class CaptionImageResponseImpl extends AlgoResponseImpl implements CaptionImageResponse {

    private final boolean isLast;
    private final boolean isSafe;
    private final String content;

    private CaptionImageResponseImpl(String uuid, Ret ret, Usage usage, boolean isLast, boolean isSafe, String content) {
        super(uuid, ret, usage);
        this.isLast = isLast;
        this.isSafe = isSafe;
        this.content = content;
    }

    @Override
    public CaptionImageResponse aggregate(CaptionImageResponse response) {
        return Optional.ofNullable(response)
                .<CaptionImageResponse>map(other -> new CaptionImageResponseImpl(
                        other.uuid(),
                        other.ret(),
                        other.usage(),
                        other.isLast(),
                        other.isSafe(),
                        this.content() + other.content()
                ))
                .orElse(this);
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

    @JsonCreator
    static CaptionImageResponseImpl of(

            @JsonProperty("id")
            String uuid,

            @JsonProperty("error_code")
            String code,

            @JsonProperty("error_msg")
            String msg,

            @JsonProperty(value = "is_end", defaultValue = "true")
            boolean isLast,

            @JsonProperty(value = "is_safe", defaultValue = "true")
            boolean isSafe,

            @JsonProperty("result")
            String content,

            @JsonProperty("usage")
            Usage usage

    ) {
        return new CaptionImageResponseImpl(uuid, Ret.of(code, msg), usage, isLast, isSafe, content);
    }

}
