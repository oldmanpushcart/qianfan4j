package io.github.ompc.erniebot4j.chat;

import io.github.ompc.erniebot4j.executor.Model;

/**
 * 对话模型
 *
 * @param name   名称
 * @param remote 远程地址
 */
public record ChatModel(String name, String remote) implements Model {

    public static final ChatModel ERNIEBOT_V4 = new ChatModel(
            "ernie-bot-4.0",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro"
    );

    public static final ChatModel ERNIEBOT_8K = new ChatModel(
            "ernie-bot-8k",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie_bot_8k"
    );

    public static final ChatModel ERNIEBOT = new ChatModel(
            "ernie-bot",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions"
    );

    public static final ChatModel ERNIE_SPEED = new ChatModel(
            "ernie-speed",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/ernie_speed"
    );

    public static final ChatModel ERNIEBOT_TURBO = new ChatModel(
            "ernie-bot-turbo",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant"
    );

    public static final ChatModel YI_34B = new ChatModel(
            "yi-34b",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/yi_34b_chat"
    );

}
