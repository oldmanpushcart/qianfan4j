package io.github.ompc.erniebot4j.chat;

import io.github.ompc.erniebot4j.executor.Option;

import java.util.HashMap;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;

public interface ChatOptions {

    Option.Opt<Boolean, Boolean> IS_ENABLE_SEARCH = new Option.StdOpt<>("disable_search", Boolean.class, v -> !v);

    Option.SimpleOpt<Boolean> IS_ENABLE_CITATION = new Option.SimpleOpt<>("enable_citation", Boolean.class);

    Option.SimpleOpt<Boolean> IS_STREAM = new Option.SimpleOpt<>("stream", Boolean.class);

    Option.Opt<Float, Float> TOP_P = new Option.StdOpt<>("top_p", Float.class,
            v -> check(v, v >= 0.0f && v <= 1.0f, "TOP_P must be in range [0.0, 1.0]")
    );

    Option.Opt<Float, Float> TEMPERATURE = new Option.StdOpt<>("temperature", Float.class,
            v -> check(v, v > 0.0f && v <= 1.0f, "TEMPERATURE must be in range (0.0, 1.0]")
    );

    Option.Opt<Float, Float> PENALTY_SCORE = new Option.StdOpt<>("penalty_score", Float.class,
            v -> check(v, v >= 1.0f && v <= 2.0f, "PENALTY_SCORE must be in range [1.0, 2.0]")
    );

    Option.SimpleOpt<String> COSPLAY = new Option.SimpleOpt<>("system", String.class);

    Option.SimpleOpt<String[]> STOP_WORDS = new Option.SimpleOpt<>("stop", String[].class);

    Option.Opt<String, Object> RECOMMENDED_FUNCTION_NAME = new Option.StdOpt<>("tool_choice", Object.class, v -> new HashMap<>() {{
        put("type", "function");
        put("function", new HashMap<>() {{
            put("name", v);
        }});
    }});

    Option.Opt<ChatResponse.Format, String> OUTPUT_FORMAT = new Option.StdOpt<>("response_format", String.class, v -> switch (v) {
        case TEXT -> "text";
        case JSON -> "json_object";
    });

    Option.Opt<Integer, Integer> MAX_OUTPUT_TOKENS = new Option.StdOpt<>("max_output_tokens", Integer.class,
            v -> check(v, v >= 1 && v <= 2048, "MAX_OUTPUT_TOKENS must be in range [1, 2048]")
    );

}
