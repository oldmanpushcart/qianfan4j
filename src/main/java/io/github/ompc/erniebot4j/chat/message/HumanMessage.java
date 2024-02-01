package io.github.ompc.erniebot4j.chat.message;

import static io.github.ompc.erniebot4j.chat.message.Message.Role.HUMAN;

/**
 * 人类消息
 *
 * @param role    角色
 * @param content 内容
 */
record HumanMessage(Role role, String content) implements Message {

    HumanMessage(String content) {
        this(HUMAN, content);
    }

}
