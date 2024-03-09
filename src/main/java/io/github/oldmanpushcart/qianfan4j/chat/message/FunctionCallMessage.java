package io.github.oldmanpushcart.qianfan4j.chat.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.oldmanpushcart.qianfan4j.chat.FunctionCall;

/**
 * 函数调用消息
 */
class FunctionCallMessage extends MessageImpl {

    @JsonProperty("function_call")
    private final FunctionCall call;

    /**
     * 构造函数调用消息
     *
     * @param call 函数调用
     */
    FunctionCallMessage(FunctionCall call) {
        super(Role.AI, null);
        this.call = call;
    }

    /**
     * 获取函数调用
     *
     * @return 函数调用
     */
    public FunctionCall call() {
        return call;
    }

}
