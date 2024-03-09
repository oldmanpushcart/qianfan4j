package io.github.oldmanpushcart.qianfan4j.chat.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.oldmanpushcart.qianfan4j.chat.FunctionCall;

public class FunctionCallMessage extends MessageImpl {

    @JsonProperty("function_call")
    private final FunctionCall call;

    FunctionCallMessage(FunctionCall call) {
        super(Role.AI, null);
        this.call = call;
    }

    public FunctionCall call() {
        return call;
    }

}
