package io.github.ompc.erniebot4j.completion;

import io.github.ompc.erniebot4j.executor.Option;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;

public interface CompletionOptions {

    Option.SimpleOpt<Boolean> IS_STREAM = new Option.SimpleOpt<>("stream", Boolean.class);

    Option.Opt<Float, Float> TEMPERATURE = new Option.StdOpt<>("temperature", Float.class,
            v -> check(v, v > 0.0f && v <= 1.0f, "TEMPERATURE must be in range (0.0, 1.0]")
    );

    Option.Opt<Float, Float> PENALTY_SCORE = new Option.StdOpt<>("penalty_score", Float.class,
            v -> check(v, v >= 1.0f && v <= 2.0f, "PENALTY_SCORE must be in range [1.0, 2.0]")
    );

    Option.Opt<Float, Float> TOP_P = new Option.StdOpt<>("top_p", Float.class,
            v -> check(v, v >= 0.0f && v <= 1.0f, "TOP_P must be in range [0.0, 1.0]")
    );

    Option.Opt<Integer, Integer> TOP_K = new Option.StdOpt<>("top_k", Integer.class,
            v -> check(v, v > 0, "TOP_K must be in range (0, Integer.MAX_VALUE]")
    );

    Option.SimpleOpt<String[]> STOP_WORDS = new Option.SimpleOpt<>("stop", String[].class);


}
