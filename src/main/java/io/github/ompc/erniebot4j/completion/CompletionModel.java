package io.github.ompc.erniebot4j.completion;

import io.github.ompc.erniebot4j.executor.Model;

/**
 * 续写模型
 *
 * @param name   模型名称
 * @param remote 模型远程地址
 */
public record CompletionModel(String name, String remote) implements Model {

    public static final CompletionModel SQL_CODER_7B = new CompletionModel(
            "sql-coder-7b",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/completions/sqlcoder_7b"
    );

}
