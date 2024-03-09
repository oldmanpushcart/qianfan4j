package io.github.oldmanpushcart.qianfan4j.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record FunctionCall(

        @JsonProperty("name")
        String name,

        @JsonProperty("arguments")
        String arguments,

        @JsonProperty("thoughts")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String thoughts

) {

    public FunctionCall(String name, String arguments) {
        this(name, arguments, null);
    }

}
