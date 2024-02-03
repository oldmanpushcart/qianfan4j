package io.github.ompc.erniebot4j.completion;

import io.github.ompc.erniebot4j.executor.Model;

public record CompletionModel(String name, String remote) implements Model {

    public static final CompletionModel SQL_CODER_7B = new CompletionModel(
            "sql-coder-7b",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/completions/sqlcoder_7b"
    );

}
