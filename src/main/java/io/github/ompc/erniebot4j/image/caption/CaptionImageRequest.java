package io.github.ompc.erniebot4j.image.caption;

import io.github.ompc.erniebot4j.executor.Model;
import io.github.ompc.erniebot4j.executor.Option;
import io.github.ompc.erniebot4j.executor.Request;

import java.awt.image.BufferedImage;
import java.time.Duration;

import static java.util.Objects.requireNonNull;

public class CaptionImageRequest implements Request {

    private final CaptionModel model;
    private final String user;
    private final Option option;
    private final Duration timeout;
    private final String prompt;
    private final BufferedImage image;

    private CaptionImageRequest(Builder builder) {
        this.model = requireNonNull(builder.model);
        this.option = builder.option;
        this.prompt = requireNonNull(builder.prompt);
        this.image = requireNonNull(builder.image);
        this.timeout = builder.timeout;
        this.user = builder.user;
    }

    @Override
    public CaptionModel model() {
        return model;
    }

    @Override
    public String user() {
        return user;
    }

    @Override
    public Option option() {
        return option;
    }

    @Override
    public Duration timeout() {
        return timeout;
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
