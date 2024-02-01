package io.github.ompc.erniebot4j.chat.message;

import static io.github.ompc.erniebot4j.chat.message.Message.Role.FUNCTION;

/**
 * 函数结果消息
 *
 * @param role    角色
 * @param name    函数名
 * @param content 内容
 */
record FunctionResultMessage(Role role, String name, String content) implements Message {

    FunctionResultMessage(String name, String content) {
        this(FUNCTION, name, content);
    }

}
