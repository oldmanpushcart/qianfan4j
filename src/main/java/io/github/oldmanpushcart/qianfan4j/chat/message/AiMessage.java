package io.github.oldmanpushcart.qianfan4j.chat.message;

/**
 * AI消息
 */
class AiMessage extends MessageImpl {

    /**
     * 构造AI消息
     *
     * @param content 内容
     */
    AiMessage(String content) {
        super(Role.AI, content);
    }

}
