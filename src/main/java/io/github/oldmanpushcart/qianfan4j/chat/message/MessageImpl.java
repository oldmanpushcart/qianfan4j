package io.github.oldmanpushcart.qianfan4j.chat.message;

/**
 * 消息实现
 */
class MessageImpl implements Message {

    private final Role role;
    private final String content;

    /**
     * 构造消息实现
     *
     * @param role    角色
     * @param content 内容
     */
    MessageImpl(Role role, String content) {
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
