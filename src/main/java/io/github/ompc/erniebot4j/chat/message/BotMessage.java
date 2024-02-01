package io.github.ompc.erniebot4j.chat.message;

import static io.github.ompc.erniebot4j.chat.message.Message.Role.BOT;

/**
 * AI消息
 *
 * @param role    角色
 * @param content 内容
 */
record BotMessage(Role role, String content) implements Message {

    BotMessage(String content) {
        this(BOT, content);
    }

}
