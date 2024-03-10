package io.github.oldmanpushcart.internal.qianfan4j.image.caption;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoRequestImpl;
import io.github.oldmanpushcart.qianfan4j.base.api.Option;
import io.github.oldmanpushcart.qianfan4j.image.caption.CaptionImageModel;
import io.github.oldmanpushcart.qianfan4j.image.caption.CaptionImageRequest;
import io.github.oldmanpushcart.qianfan4j.image.caption.CaptionImageResponse;

import java.awt.image.BufferedImage;
import java.time.Duration;

public class CaptionImageRequestImpl extends AlgoRequestImpl<CaptionImageModel, CaptionImageResponse> implements CaptionImageRequest {

    @JsonProperty("prompt")
    private final String prompt;

    @JsonSerialize(using = BufferedImageJsonSerializer.class)
    @JsonProperty("image")
    private final BufferedImage image;

    protected CaptionImageRequestImpl(Duration timeout, CaptionImageModel model, Option option, String user, String prompt, BufferedImage image) {
        super(timeout, model, option, user, CaptionImageResponseImpl.class);
        this.prompt = prompt;
        this.image = image;
    }

    @Override
    public String prompt() {
        return prompt;
    }

    @Override
    public BufferedImage image() {
        return image;
    }

}
