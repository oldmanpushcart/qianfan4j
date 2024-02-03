package io.github.ompc.erniebot4j.image.generation;

import io.github.ompc.erniebot4j.executor.Model;

public record GenImageModel(String name, String remote) implements Model {
    
    public static final GenImageModel STABLE_DIFFUSION_XL = new GenImageModel(
            "stable-diffusion-xl",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/text2image/sd_xl"
    );

}
