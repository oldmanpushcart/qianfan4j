package io.github.ompc.erniebot4j.image.caption;

import io.github.ompc.erniebot4j.executor.Model;
import io.github.ompc.erniebot4j.executor.Option;
import io.github.ompc.erniebot4j.executor.Request;

import java.awt.image.BufferedImage;
import java.time.Duration;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElseGet;

public class CaptionImageRequest implements Request {

    private final Model model;
    private final String user;
    private final Option options;
    private final Duration timeout;
    private final String prompt;
    private final BufferedImage image;

    private CaptionImageRequest(Builder builder) {
        this.model = requireNonNull(builder.model);
        this.options = requireNonNullElseGet(builder.options, Option::new);
        this.prompt = requireNonNull(builder.prompt);
        this.image = requireNonNull(builder.image);
        this.timeout = builder.timeout;
        this.user = builder.user;
    }

    @Override
    public Model model() {
        return model;
    }

    @Override
    public String user() {
        return user;
    }

    @Override
    public Option options() {
        return options;
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

    public <T, R> CaptionImageRequest option(Option.Opt<T, R> opt, T value) {
        options.option(opt, value);
        return this;
    }

    public static class Builder {
        private Model model;
        private String user;
        private Option options;
        private Duration timeout;
        private String prompt;
        private BufferedImage image;

        public Builder model(Model model) {
            this.model = model;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder options(Option options) {
            this.options = options;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder image(BufferedImage image) {
            this.image = image;
            return this;
        }

        public CaptionImageRequest build() {
            return new CaptionImageRequest(this);
        }

    }

}
