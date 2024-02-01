package io.github.ompc.erniebot4j.chat.message;

/**
 * 对话消息
 */
public interface Message {

    /**
     * 获取角色
     *
     * @return 角色
     */
    Role role();

    /**
     * 获取内容
     *
     * @return 内容
     */
    String content();

    /**
     * 人类消息
     *
     * @param content 内容
     * @return 消息
     */
    static Message human(String content) {
        return new HumanMessage(content);
    }

    /**
     * AI消息
     *
     * @param content 内容
     * @return 消息
     */
    static Message bot(String content) {
        return new BotMessage(content);
    }

    /**
     * 函数调用消息
     *
     * @param name      函数名
     * @param arguments 参数
     * @return 消息
     */
    static Message functionCall(String name, String arguments) {
        return new FunctionCallMessage(name, arguments);
    }

    /**
     * 函数结果消息
     *
     * @param name    函数名
     * @param content 内容
     * @return 消息
     */
    static Message functionResult(String name, String content) {
        return new FunctionResultMessage(name, content);
    }

    /**
     * 角色
     */
    enum Role {

        /**
         * 人类
         */
        HUMAN,

        /**
         * AI
         */
        BOT,

        /**
         * 函数
         */
        FUNCTION,

    }

}
