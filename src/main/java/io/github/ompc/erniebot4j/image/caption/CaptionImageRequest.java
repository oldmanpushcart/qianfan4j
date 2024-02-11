package io.github.ompc.erniebot4j.image.caption;

import io.github.ompc.erniebot4j.executor.BaseRequest;
import io.github.ompc.erniebot4j.executor.Request;

import java.awt.image.BufferedImage;

import static java.util.Objects.requireNonNull;

public final class CaptionImageRequest extends BaseRequest<CaptionModel> implements Request {

    private final String prompt;
    private final BufferedImage image;

    private CaptionImageRequest(Builder builder) {
        super(builder);
        this.prompt = requireNonNull(builder.prompt);
        this.image = requireNonNull(builder.image);
    }

    public String prompt() {
        return prompt;
    }

    public BufferedImage image() {
        return image;
    }

    public static class Builder extends BaseRequest.BaseBuilder<CaptionModel, CaptionImageRequest, Builder> {

        private String prompt;
        private BufferedImage image;

        public Builder prompt(String prompt) {
            this.prompt = requireNonNull(prompt);
            return this;
        }

        public Builder image(BufferedImage image) {
            this.image = requireNonNull(image);
            return this;
        }

        @Override
        public CaptionImageRequest build() {
            return new CaptionImageRequest(this);
        }

    }

}
