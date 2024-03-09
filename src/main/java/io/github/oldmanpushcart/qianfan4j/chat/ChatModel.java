package io.github.oldmanpushcart.qianfan4j.chat;

import io.github.oldmanpushcart.qianfan4j.base.algo.Model;

/**
 * 对话模型
 *
 * @param name   名称
 * @param remote 远程地址
 */
public record ChatModel(String name, String remote) implements Model {

    public static final ChatModel ERNIE_V4 = new ChatModel(
            "ernie-v4.0",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro"
    );

    public static ChatModel ERNIE_V3$5_8K = new ChatModel(
            "ernie-v3.5-8k",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie_bot_8k"
    );

    public static ChatModel ERNIE_V3$5_8K_0225 = new ChatModel(
            "ernie-v3.5-8k-0205",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-3.5-8k-0205"
    );

    public static ChatModel ERNIE_V3$5_8K_1222 = new ChatModel(
            "ernie-v3.5-8k-1222",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-3.5-8k-1222"
    );

    public static ChatModel ERNIE_V3$5_4K_0205 = new ChatModel(
            "ernie-v3.5-4k-0205",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie-3.5-4k-0205"
    );

}
