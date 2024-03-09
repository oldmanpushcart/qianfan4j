package io.github.oldmanpushcart.qianfan4j.chat.message;

/**
 * 用户消息
 */
class UserMessage extends MessageImpl {

    /**
     * 构造用户消息
     * @param content 内容
     */
    UserMessage(String content) {
        super(Role.USER, content);
    }

}
