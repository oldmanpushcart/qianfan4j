package io.github.ompc.erniebot4j.executor;

import io.github.ompc.erniebot4j.executor.Option.SimpleOpt;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;

public interface Options {

    SimpleOpt<Float> TEMPERATURE = new SimpleOpt<>("temperature", Float.class,
            v -> check(v, v > 0.0f && v <= 1.0f, "TEMPERATURE must be in range (0.0, 1.0]")
    );

    SimpleOpt<Float> TOP_P = new SimpleOpt<>("top_p", Float.class,
            v -> check(v, v >= 0.0f && v <= 1.0f, "TOP_P must be in range [0.0, 1.0]")
    );

    SimpleOpt<Integer> TOP_K = new SimpleOpt<>("top_k", Integer.class,
            v -> check(v, v > 0, "TOP_K must be in range (0, Integer.MAX_VALUE]")
    );

    SimpleOpt<Float> PENALTY_SCORE = new SimpleOpt<>("penalty_score", Float.class,
            v -> check(v, v >= 1.0f && v <= 2.0f, "PENALTY_SCORE must be in range [1.0, 2.0]")
    );

    SimpleOpt<Boolean> IS_STREAM = new SimpleOpt<>("stream", Boolean.class);

    SimpleOpt<String[]> STOP_WORDS = new SimpleOpt<>("stop", String[].class);

}
