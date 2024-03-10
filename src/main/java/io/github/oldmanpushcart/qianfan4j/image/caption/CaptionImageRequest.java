package io.github.oldmanpushcart.qianfan4j.image.caption;

import io.github.oldmanpushcart.internal.qianfan4j.image.caption.CaptionImageRequestBuilderImpl;
import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoRequest;

import java.awt.image.BufferedImage;

/**
 * 图生文请求
 */
public interface CaptionImageRequest extends AlgoRequest<CaptionImageModel, CaptionImageResponse> {

    /**
     * 获取提示
     *
     * @return 提示
     */
    String prompt();

    /**
     * 获取图片
     *
     * @return 图片
     */
    BufferedImage image();

    /**
     * 创建构建器
     *
     * @return 构建器
     */
    static Builder newBuilder() {
        return new CaptionImageRequestBuilderImpl();
    }

    /**
     * 图生文构建器
     */
    interface Builder extends AlgoRequest.Builder<CaptionImageModel, CaptionImageRequest, Builder> {

        /**
         * 设置提示
         *
         * @param prompt 提示
         * @return this
         */
        Builder prompt(String prompt);

        /**
         * 设置图片
         *
         * @param image 图片
         * @return this
         */
        Builder image(BufferedImage image);

    }

}
