package io.github.oldmanpushcart.qianfan4j.image.generation;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.oldmanpushcart.internal.qianfan4j.image.generation.GenerationImageRequestBuilderImpl;
import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoRequest;

/**
 * 文生图请求
 */
public interface GenerationImageRequest extends AlgoRequest<GenerationImageModel, GenerationImageResponse> {

    /**
     * 获取提示
     *
     * @return 提示
     */
    String prompt();

    /**
     * 获取负面提示
     *
     * @return 负面提示
     */
    String negative();

    /**
     * 创建构建器
     *
     * @return 构建器
     */
    static Builder newBuilder() {
        return new GenerationImageRequestBuilderImpl();
    }

    /**
     * 文生图请求构建器
     */
    interface Builder extends AlgoRequest.Builder<GenerationImageModel, GenerationImageRequest, GenerationImageRequest.Builder> {

        /**
         * 设置提示
         *
         * @param prompt 提示
         * @return this
         */
        Builder prompt(String prompt);

        /**
         * 设置负面提示
         *
         * @param negative 负面提示
         * @return this
         */
        Builder negative(String negative);

    }

    /**
     * 图片尺寸
     */
    enum Size {

        @JsonProperty("768x768")
        S_768_768,

        @JsonProperty("768x1024")
        S_768_1024,

        @JsonProperty("1024x768")
        S_1024_768,

        @JsonProperty("576x1024")
        S_576_1024,

        @JsonProperty("1024x576")
        S_1024_576,

        @JsonProperty("1024x1024")
        S_1024_1024;

    }

    /**
     * 图片采样器
     */
    enum Sampler {

        @JsonProperty("Euler")
        EULER,

        @JsonProperty("Euler a")
        EULER_A,

        @JsonProperty("DPM++ 2M")
        DPM_2M,

        @JsonProperty("DPM++ 2M Karras")
        DPM_2M_KARRAS,

        @JsonProperty("LMS Karras")
        LMS_KARRAS,

        @JsonProperty("DPM++ SDE")
        DPM_SDE,

        @JsonProperty("DPM++ SDE Karras")
        DPM_SDE_KARRAS,

        @JsonProperty("DPM2 a Karras")
        DPM2_A_KARRAS,

        @JsonProperty("Heun")
        HEUN,

        @JsonProperty("DPM++ 2M SDE")
        DPM_2M_SDE,

        @JsonProperty("DPM++ 2M SDE Karras")
        DPM_2M_SDE_KARRAS,

        @JsonProperty("DPM2")
        DPM2,

        @JsonProperty("DPM2 Karras")
        DPM2_KARRAS,

        @JsonProperty("DPM2 a")
        DPM2_A,

        @JsonProperty("LMS")
        LMS

    }

    /**
     * 图片风格
     */
    enum Style {

        @JsonProperty("Base")
        BASE,

        @JsonProperty("3D Model")
        THREE_DIMENSIONAL_MODEL,

        @JsonProperty("Analog Film")
        ANALOG_FILM,

        @JsonProperty("Anime")
        ANIME,

        @JsonProperty("Cinematic")
        CINEMATIC,

        @JsonProperty("Comic Book")
        COMIC_BOOK,

        @JsonProperty("Craft Clay")
        CRAFT_CLAY,

        @JsonProperty("Digital Art")
        DIGITAL_ART,

        @JsonProperty("Enhance")
        ENHANCE,

        @JsonProperty("Fantasy Art")
        FANTASY_ART,

        @JsonProperty("lsometric")
        ISOMETRIC,

        @JsonProperty("Line Art")
        LINE_ART,

        @JsonProperty("Lowpoly")
        LOWPOLY,

        @JsonProperty("Neonpunk")
        NEONPUNK,

        @JsonProperty("Origami")
        ORIGAMI,

        @JsonProperty("Photographic")
        PHOTOGRAPHIC,

        @JsonProperty("Pixel Art")
        PIXEL_ART,

        @JsonProperty("Texture")
        TEXTURE

    }

}
