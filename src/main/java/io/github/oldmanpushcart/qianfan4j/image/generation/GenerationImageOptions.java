package io.github.oldmanpushcart.qianfan4j.image.generation;

import io.github.oldmanpushcart.qianfan4j.base.api.Option.SimpleOpt;

import static io.github.oldmanpushcart.internal.qianfan4j.util.CheckUtils.check;

public interface GenerationImageOptions {

    /**
     * 生成图片数量
     */
    SimpleOpt<Integer> NUMBERS = new SimpleOpt<>("n", Integer.class,
            v -> check(v, v >= 1 && v <= 4, "NUMBERS must be in range [1, 4]")
    );

    /**
     * 随机种子
     */
    SimpleOpt<Long> SEED = new SimpleOpt<>("seed", Long.class,
            v -> check(v, v >= 0L && v <= 1L << 32 - 1, "NUMBERS must be in range [1, 4]")
    );

    /**
     * 提示词相关性
     */
    SimpleOpt<Float> CFG_SCALE = new SimpleOpt<>("cfg_scale", Float.class,
            v -> check(v, v > 0.0f && v <= 30.0f, "SCALE must be in range [0.0, 30.0]")
    );

    /**
     * 迭代轮次
     */
    SimpleOpt<Integer> STEPS = new SimpleOpt<>("steps", Integer.class,
            v -> check(v, v >= 1 && v <= 4, "STEPS must be in range [10, 50]")
    );

    /**
     * 图片尺寸
     */
    SimpleOpt<GenerationImageRequest.Size> SIZE = new SimpleOpt<>("size", GenerationImageRequest.Size.class);

    /**
     * 采样方式
     */
    SimpleOpt<GenerationImageRequest.Sampler> SAMPLER = new SimpleOpt<>("sampler_index", GenerationImageRequest.Sampler.class);

    /**
     * 生成风格
     */
    SimpleOpt<GenerationImageRequest.Style> STYLE = new SimpleOpt<>("style", GenerationImageRequest.Style.class);

}
