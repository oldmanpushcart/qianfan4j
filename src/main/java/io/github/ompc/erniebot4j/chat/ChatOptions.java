package io.github.ompc.erniebot4j.chat;

import io.github.ompc.erniebot4j.executor.Option.Opt;
import io.github.ompc.erniebot4j.executor.Option.SimpleOpt;
import io.github.ompc.erniebot4j.executor.Option.StdOpt;
import io.github.ompc.erniebot4j.executor.Options;

import java.util.HashMap;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;

public interface ChatOptions extends Options {

    SimpleOpt<Boolean> IS_ENABLE_SEARCH = new SimpleOpt<>("disable_search", Boolean.class, v -> !v);

    SimpleOpt<Boolean> IS_ENABLE_CITATION = new SimpleOpt<>("enable_citation", Boolean.class);

    SimpleOpt<String> COSPLAY = new SimpleOpt<>("system", String.class);

    Opt<String, Object> RECOMMENDED_FUNCTION_NAME = new StdOpt<>("tool_choice", Object.class, v -> new HashMap<>() {{
        put("type", "function");
        put("function", new HashMap<>() {{
            put("name", v);
        }});
    }});

    SimpleOpt<ChatResponse.Format> OUTPUT_FORMAT = new SimpleOpt<>("response_format", ChatResponse.Format.class);

    SimpleOpt<Integer> MAX_OUTPUT_TOKENS = new SimpleOpt<>("max_output_tokens", Integer.class,
            v -> check(v, v >= 1 && v <= 2048, "MAX_OUTPUT_TOKENS must be in range [1, 2048]")
    );

}
