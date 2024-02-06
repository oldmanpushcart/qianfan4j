package io.github.ompc.erniebot4j.chat;

import io.github.ompc.erniebot4j.executor.Option.Opt;
import io.github.ompc.erniebot4j.executor.Option.SimpleOpt;
import io.github.ompc.erniebot4j.executor.Option.StdOpt;
import io.github.ompc.erniebot4j.executor.Options;

import java.util.HashMap;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;

/**
 * 对话选项
 */
public interface ChatOptions extends Options {

    /**
     * 是否启用搜索
     */
    SimpleOpt<Boolean> IS_ENABLE_SEARCH = new SimpleOpt<>("disable_search", Boolean.class, v -> !v);

    /**
     * 是否启用引用
     */
    SimpleOpt<Boolean> IS_ENABLE_CITATION = new SimpleOpt<>("enable_citation", Boolean.class);

    /**
     * 设置扮演角色
     */
    SimpleOpt<String> COSPLAY = new SimpleOpt<>("system", String.class);

    /**
     * 设置推荐函数名
     */
    Opt<String, Object> RECOMMENDED_FUNCTION_NAME = new StdOpt<>("tool_choice", Object.class, v -> new HashMap<>() {{
        put("type", "function");
        put("function", new HashMap<>() {{
            put("name", v);
        }});
    }});

    /**
     * 设置输出格式
     */
    SimpleOpt<ChatResponse.Format> OUTPUT_FORMAT = new SimpleOpt<>("response_format", ChatResponse.Format.class);

    /**
     * 设置单次对话最大TOKEN使用量
     */
    SimpleOpt<Integer> MAX_OUTPUT_TOKENS = new SimpleOpt<>("max_output_tokens", Integer.class,
            v -> check(v, v >= 1 && v <= 2048, "MAX_OUTPUT_TOKENS must be in range [1, 2048]")
    );

}
