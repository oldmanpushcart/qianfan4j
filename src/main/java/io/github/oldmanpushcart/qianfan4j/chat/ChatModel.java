package io.github.oldmanpushcart.qianfan4j.chat;

import io.github.oldmanpushcart.qianfan4j.base.algo.Model;

public record ChatModel(String name, String remote) implements Model {

    public static final ChatModel ERNIEBOT_V4 = new ChatModel(
            "ernie-bot-4.0",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro"
    );

    public static ChatModel ERNIEBOT_8K = new ChatModel(
            "ernie-bot-8k",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie_bot_8k"
    );

}
