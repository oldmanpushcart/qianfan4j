package io.github.oldmanpushcart.qianfan4j.chat;

import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoOptions;
import io.github.oldmanpushcart.qianfan4j.base.api.Option;

import java.util.HashMap;

import static io.github.oldmanpushcart.internal.qianfan4j.util.CheckUtils.check;

/**
 * 对话选项
 */
public interface ChatOptions extends AlgoOptions {

    /**
     * 停止词；当模型生成结果以停止词集合中某个元素结尾时，停止文本生成。
     * <ul>
     *     <li>每个停止词长度不超过20字符</li>
     *     <li>最多4个停止词</li>
     * </ul>
     */
    Option.SimpleOpt<String[]> STOP_WORDS = new Option.SimpleOpt<>("stop", String[].class);

    /**
     * 是否启用搜索
     */
    Option.SimpleOpt<Boolean> IS_ENABLE_SEARCH = new Option.SimpleOpt<>("disable_search", Boolean.class, v -> !v);

    /**
     * 是否启用引用
     */
    Option.SimpleOpt<Boolean> IS_ENABLE_CITATION = new Option.SimpleOpt<>("enable_citation", Boolean.class);

    /**
     * 设置扮演角色
     */
    Option.SimpleOpt<String> COSPLAY = new Option.SimpleOpt<>("system", String.class);

    /**
     * 设置推荐函数名
     */
    Option.Opt<String, Object> RECOMMENDED_FUNCTION_NAME = new Option.StdOpt<>("tool_choice", Object.class, v -> new HashMap<>() {{
        put("type", "function");
        put("function", new HashMap<>() {{
            put("name", v);
        }});
    }});

    /**
     * 设置输出格式
     */
    Option.SimpleOpt<ChatResponse.Format> OUTPUT_FORMAT = new Option.SimpleOpt<>("response_format", ChatResponse.Format.class);

    /**
     * 设置单次对话最大TOKEN使用量
     */
    Option.SimpleOpt<Integer> MAX_OUTPUT_TOKENS = new Option.SimpleOpt<>("max_output_tokens", Integer.class,
            v -> check(v, v >= 1 && v <= 2048, "MAX_OUTPUT_TOKENS must be in range [1, 2048]")
    );

}
