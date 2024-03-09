package io.github.oldmanpushcart.qianfan4j.chat.message;

public class MessageImpl implements Message {

    private final Role role;
    private final String content;

    public MessageImpl(Role role, String content) {
        this.role = role;
        this.content = content;
    }

    @Override
    public Role role() {
        return role;
    }

    @Override
    public String content() {
        return content;
    }

}
