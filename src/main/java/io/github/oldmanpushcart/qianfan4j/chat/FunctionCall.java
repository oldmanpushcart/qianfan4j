package io.github.oldmanpushcart.qianfan4j.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 函数调用
 *
 * @param name      函数名称
 * @param arguments 参数
 * @param thoughts  想法
 */
public record FunctionCall(

        @JsonProperty("name")
        String name,

        @JsonProperty("arguments")
        String arguments,

        @JsonProperty("thoughts")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String thoughts

) {

    /**
     * 函数调用
     *
     * @param name      函数名称
     * @param arguments 参数
     */
    public FunctionCall(String name, String arguments) {
        this(name, arguments, null);
    }

}
