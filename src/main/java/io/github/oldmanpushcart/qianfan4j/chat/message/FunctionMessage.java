package io.github.oldmanpushcart.qianfan4j.chat.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 函数消息
 */
class FunctionMessage extends MessageImpl {

    @JsonProperty("name")
    private final String name;

    /**
     * 构造函数消息
     *
     * @param name    函数名称
     * @param content 函数内容
     */
    FunctionMessage(String name, String content) {
        super(Role.FUNCTION, content);
        this.name = name;
    }

    /**
     * 获取函数名称
     *
     * @return 函数名称
     */
    public String name() {
        return name;
    }

}
