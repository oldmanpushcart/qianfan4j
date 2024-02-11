package io.github.ompc.erniebot4j.image.generation;

import io.github.ompc.erniebot4j.executor.Option.SimpleOpt;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;

/**
 * 文生图选项
 */
public interface GenImageOptions {

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
    SimpleOpt<GenImageRequest.Size> SIZE = new SimpleOpt<>("size", GenImageRequest.Size.class);

    /**
     * 采样方式
     */
    SimpleOpt<GenImageRequest.Sampler> SAMPLER = new SimpleOpt<>("sampler_index", GenImageRequest.Sampler.class);

    /**
     * 生成风格
     */
    SimpleOpt<GenImageRequest.Style> STYLE = new SimpleOpt<>("style", GenImageRequest.Style.class);


}
