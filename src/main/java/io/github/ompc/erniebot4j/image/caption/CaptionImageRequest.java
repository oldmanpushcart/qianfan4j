package io.github.ompc.erniebot4j.image.caption;

import io.github.ompc.erniebot4j.executor.BaseRequest;
import io.github.ompc.erniebot4j.executor.Request;

import java.awt.image.BufferedImage;

import static java.util.Objects.requireNonNull;

/**
 * 图生文请求
 */
public final class CaptionImageRequest extends BaseRequest<CaptionModel> implements Request {

    private final String prompt;
    private final BufferedImage image;

    private CaptionImageRequest(Builder builder) {
        super(builder);
        this.prompt = requireNonNull(builder.prompt);
        this.image = requireNonNull(builder.image);
    }

    /**
     * 获取提示
     *
     * @return 提示
     */
    public String prompt() {
        return prompt;
    }

    /**
     * 获取图片
     *
     * @return 图片
     */
    public BufferedImage image() {
        return image;
    }

    /**
     * 图生文构建器
     */
    public static class Builder extends BaseRequest.BaseBuilder<CaptionModel, CaptionImageRequest, Builder> {

        private String prompt;
        private BufferedImage image;

        /**
         * 设置提示
         *
         * @param prompt 提示
         * @return this
         */
        public Builder prompt(String prompt) {
            this.prompt = requireNonNull(prompt);
            return this;
        }

        /**
         * 设置图片
         *
         * @param image 图片
         * @return this
         */
        public Builder image(BufferedImage image) {
            this.image = requireNonNull(image);
            return this;
        }

        /**
         * 构建图生文请求
         *
         * @return 图生文请求
         */
        @Override
        public CaptionImageRequest build() {
            return new CaptionImageRequest(this);
        }

    }

}
