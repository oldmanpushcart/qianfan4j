package io.github.oldmanpushcart.internal.qianfan4j.image.caption;

import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoRequestBuilderImpl;
import io.github.oldmanpushcart.qianfan4j.image.caption.CaptionImageModel;
import io.github.oldmanpushcart.qianfan4j.image.caption.CaptionImageRequest;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class CaptionImageRequestBuilderImpl extends AlgoRequestBuilderImpl<CaptionImageModel, CaptionImageRequest, CaptionImageRequest.Builder>
        implements CaptionImageRequest.Builder {

    private String prompt;
    private BufferedImage image;

    @Override
    public CaptionImageRequest.Builder prompt(String prompt) {
        this.prompt = Objects.requireNonNull(prompt);
        return this;
    }

    @Override
    public CaptionImageRequest.Builder image(BufferedImage image) {
        this.image = Objects.requireNonNull(image);
        return this;
    }

    @Override
    public CaptionImageRequest build() {
        return new CaptionImageRequestImpl(
                timeout(),
                model(),
                option(),
                user(),
                Objects.requireNonNull(prompt),
                Objects.requireNonNull(image)
        );
    }

}
