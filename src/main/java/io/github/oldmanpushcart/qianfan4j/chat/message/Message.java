package io.github.oldmanpushcart.qianfan4j.chat.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.oldmanpushcart.qianfan4j.chat.FunctionCall;

/**
 * 对话消息
 */
public interface Message {

    /**
     * 获取角色
     *
     * @return 角色
     */
    @JsonProperty("role")
    Role role();

    /**
     * 获取内容
     *
     * @return 内容
     */
    @JsonProperty("content")
    String content();

    /**
     * AI消息(文本)
     *
     * @param text 文本
     * @return 消息
     */
    static Message ofAi(String text) {
        return new AiMessage(text);
    }

    /**
     * 用户消息(文本)
     *
     * @param text 文本
     * @return 消息
     */
    static Message ofUser(String text) {
        return new UserMessage(text);
    }

    /**
     * 函数消息
     *
     * @param name    函数名称
     * @param content 函数内容
     * @return 消息
     */
    static Message ofFunction(String name, String content) {
        return new FunctionMessage(name, content);
    }

    /**
     * 函数调用消息
     *
     * @param call 函数调用
     * @return 消息
     */
    static Message ofFunctionCall(FunctionCall call) {
        return new FunctionCallMessage(call);
    }

    /**
     * 角色
     */
    enum Role {

        /**
         * AI
         */
        @JsonProperty("assistant")
        AI,

        /**
         * 用户
         */
        @JsonProperty("user")
        USER,

        /**
         * 插件
         */
        @JsonProperty("function")
        FUNCTION

    }

}
