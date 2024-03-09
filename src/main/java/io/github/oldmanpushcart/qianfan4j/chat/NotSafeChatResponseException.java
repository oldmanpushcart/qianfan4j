package io.github.oldmanpushcart.qianfan4j.chat;

/**
 * 不安全的对话应答异常
 */
public class NotSafeChatResponseException extends ChatException {

    private final ChatResponse response;

    /**
     * 不安全的对话应答异常
     *
     * @param response 聊天应答
     */
    public NotSafeChatResponseException(ChatResponse response) {
        this.response = response;
    }

    /**
     * 获取对话应答
     *
     * @return 对话应答
     */
    public ChatResponse response() {
        return response;
    }

}
