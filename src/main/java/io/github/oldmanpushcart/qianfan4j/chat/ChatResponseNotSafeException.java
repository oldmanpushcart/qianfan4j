package io.github.oldmanpushcart.qianfan4j.chat;

public class ChatResponseNotSafeException extends ChatException {

    private final ChatResponse response;

    public ChatResponseNotSafeException(ChatResponse response) {
        this.response = response;
    }

    public ChatResponse response() {
        return response;
    }

}
