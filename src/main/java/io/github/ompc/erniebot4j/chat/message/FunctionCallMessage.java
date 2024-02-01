package io.github.ompc.erniebot4j.chat.message;

import static io.github.ompc.erniebot4j.chat.message.Message.Role.BOT;

/**
 * 函数调用消息
 *
 * @param role         角色
 * @param content      内容
 * @param functionCall 函数调用
 */
record FunctionCallMessage(Role role, String content, FunctionCall functionCall) implements Message {

    FunctionCallMessage(String name, String arguments) {
        this(BOT, null, new FunctionCall(name, arguments));
    }

    record FunctionCall(String name, String arguments) {

    }

}
