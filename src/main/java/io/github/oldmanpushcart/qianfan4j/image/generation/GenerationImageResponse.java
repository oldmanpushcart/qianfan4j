package io.github.oldmanpushcart.qianfan4j.image.generation;

import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoResponse;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 文生图应答
 */
public interface GenerationImageResponse extends AlgoResponse {

    /**
     * 生成图片列表
     *
     * @return 图片列表
     */
    List<BufferedImage> images();

}
