package io.github.oldmanpushcart.qianfan4j.chat.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FunctionMessage extends MessageImpl {

    @JsonProperty("name")
    private final String name;

    FunctionMessage(String name, String content) {
        super(Role.FUNCTION, content);
        this.name = name;
    }

    public String name() {
        return name;
    }

}
