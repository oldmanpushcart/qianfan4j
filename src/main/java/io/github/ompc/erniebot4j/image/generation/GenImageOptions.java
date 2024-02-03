package io.github.ompc.erniebot4j.image.generation;

import io.github.ompc.erniebot4j.executor.Option;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;

public interface GenImageOptions {

    Option.Opt<Integer, Integer> NUMBERS = new Option.StdOpt<>("n", Integer.class,
            v -> check(v, v >= 1 && v <= 4, "NUMBERS must be in range [1, 4]")
    );

    Option.Opt<Long, Long> SEED = new Option.StdOpt<>("seed", Long.class,
            v -> check(v, v >= 0L && v <= 1L << 32 - 1, "NUMBERS must be in range [1, 4]")
    );

    Option.Opt<Float, Float> SCALE = new Option.StdOpt<>("scale", Float.class,
            v -> check(v, v > 0.0f && v <= 30.0f, "SCALE must be in range [0.0, 30.0]")
    );

    Option.Opt<Integer, Integer> STEPS = new Option.StdOpt<>("steps", Integer.class,
            v -> check(v, v >= 1 && v <= 4, "STEPS must be in range [10, 50]")
    );

    Option.SimpleOpt<GenImageRequest.Size> SIZE = new Option.SimpleOpt<>("size", GenImageRequest.Size.class);

    Option.SimpleOpt<GenImageRequest.Sampler> SAMPLER = new Option.SimpleOpt<>("sampler_index", GenImageRequest.Sampler.class);

    Option.SimpleOpt<GenImageRequest.Style> STYLE = new Option.SimpleOpt<>("style", GenImageRequest.Style.class);


}
