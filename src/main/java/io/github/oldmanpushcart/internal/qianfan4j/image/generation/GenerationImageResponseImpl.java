package io.github.oldmanpushcart.internal.qianfan4j.image.generation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoResponseImpl;
import io.github.oldmanpushcart.qianfan4j.base.algo.Usage;
import io.github.oldmanpushcart.qianfan4j.base.api.Ret;
import io.github.oldmanpushcart.qianfan4j.image.generation.GenerationImageResponse;

import java.awt.image.BufferedImage;
import java.util.List;

public class GenerationImageResponseImpl extends AlgoResponseImpl implements GenerationImageResponse {

    private final List<BufferedImage> images;

    private GenerationImageResponseImpl(String uuid, Ret ret, Usage usage, List<BufferedImage> images) {
        super(uuid, ret, usage);
        this.images = images;
    }

    @Override
    public List<BufferedImage> images() {
        return images;
    }

    @JsonCreator
    static GenerationImageResponseImpl of(

            @JsonProperty("id")
            String uuid,

            @JsonProperty("error_code")
            String code,

            @JsonProperty("error_msg")
            String msg,

            @JsonProperty("usage")
            Usage usage,

            @JsonDeserialize(contentUsing = BufferedImageJsonDeserializer.class)
            @JsonProperty("data")
            List<BufferedImage> images

    ) {
        return new GenerationImageResponseImpl(uuid, Ret.of(code, msg), usage, images);
    }

}
