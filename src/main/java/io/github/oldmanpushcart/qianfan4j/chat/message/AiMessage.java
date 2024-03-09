package io.github.oldmanpushcart.qianfan4j.chat.message;

public class AiMessage extends MessageImpl {

    AiMessage(String content) {
        super(Role.AI, content);
    }

}
