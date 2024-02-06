package io.github.ompc.erniebot4j.image.caption;

import io.github.ompc.erniebot4j.executor.BaseRequest;
import io.github.ompc.erniebot4j.executor.Option;
import io.github.ompc.erniebot4j.executor.Request;

import java.awt.image.BufferedImage;
import java.time.Duration;

import static java.util.Objects.requireNonNull;

public class CaptionImageRequest extends BaseRequest implements Request {

    private final String prompt;
    private final BufferedImage image;

    private CaptionImageRequest(Builder builder) {
        super(
                requireNonNull(builder.model),
                builder.option,
                builder.timeout,
                builder.user
        );
        this.prompt = requireNonNull(builder.prompt);
        this.image = requireNonNull(builder.image);
    }

    public String prompt() {
        return prompt;
    }

    public BufferedImage image() {
        return image;
    }

    public static class Builder {
        private CaptionModel model;
        private String user;
        private final Option option = new Option();
        private Duration timeout;
        private String prompt;
        private BufferedImage image;

        public Builder model(CaptionModel model) {
            this.model = requireNonNull(model);
            return this;
        }

        public Builder user(String user) {
            this.user = requireNonNull(user);
            return this;
        }

        public Builder option(Option option) {
            this.option.load(option);
            return this;
        }

        public <T, R> Builder option(Option.Opt<T, R> opt, T value) {
            option.option(opt, value);
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = requireNonNull(timeout);
            return this;
        }

        public Builder prompt(String prompt) {
            this.prompt = requireNonNull(prompt);
            return this;
        }

        public Builder image(BufferedImage image) {
            this.image = requireNonNull(image);
            return this;
        }

        public CaptionImageRequest build() {
            return new CaptionImageRequest(this);
        }

    }

}
