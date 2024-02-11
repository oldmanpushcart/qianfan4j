package io.github.ompc.erniebot4j.image.generation;

import io.github.ompc.erniebot4j.executor.BaseRequest;
import io.github.ompc.erniebot4j.executor.Request;
import io.github.ompc.erniebot4j.executor.Textualizable;

import static java.util.Objects.requireNonNull;

/**
 * 文生图请求
 */
public final class GenImageRequest extends BaseRequest<GenImageModel> implements Request {

    private final String prompt;
    private final String negative;

    private GenImageRequest(Builder builder) {
        super(builder);
        this.prompt = requireNonNull(builder.prompt);
        this.negative = builder.negative;
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
     * 获取负面提示
     *
     * @return 负面提示
     */
    public String negative() {
        return negative;
    }

    /**
     * 文生图构建器
     */
    public static class Builder extends BaseRequest.BaseBuilder<GenImageModel, GenImageRequest, Builder> {
        private String prompt;
        private String negative;

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
         * 设置负面提示
         *
         * @param negative 负面提示
         * @return this
         */
        public Builder negative(String negative) {
            this.negative = requireNonNull(negative);
            return this;
        }

        /**
         * 构建文生图请求
         *
         * @return 文生图请求
         */
        @Override
        public GenImageRequest build() {
            return new GenImageRequest(this);
        }
    }

    /**
     * 图片尺寸
     */
    public enum Size implements Textualizable {
        S_768_768("768x768"),
        S_768_1024("768x1024"),
        S_1024_768("1024x768"),
        S_576_1024("576x1024"),
        S_1024_576("1024x576"),
        S_1024_1024("1024x1024");

        private final String text;

        Size(String text) {
            this.text = text;
        }

        @Override
        public String getText() {
            return text;
        }

    }

    /**
     * 图片采样器
     */
    public enum Sampler implements Textualizable {

        EULER("Euler"),
        EULER_A("Euler a"),
        DPM_2M("DPM++ 2M"),
        DPM_2M_KARRAS("DPM++ 2M Karras"),
        LMS_KARRAS("LMS Karras"),
        DPM_SDE("DPM++ SDE"),
        DPM_SDE_KARRAS("DPM++ SDE Karras"),
        DPM2_A_KARRAS("DPM2 a Karras"),
        HEUN("Heun"),
        DPM_2M_SDE("DPM++ 2M SDE"),
        DPM_2M_SDE_KARRAS("DPM++ 2M SDE Karras"),
        DPM2("DPM2"),
        DPM2_KARRAS("DPM2 Karras"),
        DPM2_A("DPM2 a"),
        LMS("LMS");

        private final String text;

        Sampler(String text) {
            this.text = text;
        }

        @Override
        public String getText() {
            return text;
        }

    }

    /**
     * 图片风格
     */
    public enum Style implements Textualizable {

        BASE("Base"),
        THREE_DIMENSIONAL_MODEL("3D Model"),
        ANALOG_FILM("Analog Film"),
        ANIME("Anime"),
        CINEMATIC("Cinematic"),
        COMIC_BOOK("Comic Book"),
        CRAFT_CLAY("Craft Clay"),
        DIGITAL_ART("Digital Art"),
        ENHANCE("Enhance"),
        FANTASY_ART("Fantasy Art"),
        ISOMETRIC("lsometric"),
        LINE_ART("Line Art"),
        LOWPOLY("Lowpoly"),
        NEONPUNK("Neonpunk"),
        ORIGAMI("Origami"),
        PHOTOGRAPHIC("Photographic"),
        PIXEL_ART("Pixel Art"),
        TEXTURE("Texture");

        private final String text;

        Style(String text) {
            this.text = text;
        }

        @Override
        public String getText() {
            return text;
        }

    }

}
