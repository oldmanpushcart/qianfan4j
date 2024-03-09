package io.github.oldmanpushcart.qianfan4j.chat.message;

public class UserMessage extends MessageImpl {

    UserMessage(String content) {
        super(Role.USER, content);
    }

}
