package io.github.oldmanpushcart.qianfan4j.image.generation;

import io.github.oldmanpushcart.qianfan4j.base.algo.Model;

/**
 * 文生图模型
 *
 * @param name   名称
 * @param remote 远程地址
 */
public record GenerationImageModel(String name, String remote) implements Model {

    public static final GenerationImageModel STABLE_DIFFUSION_XL = new GenerationImageModel(
            "stable-diffusion-xl",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/text2image/sd_xl"
    );

}
