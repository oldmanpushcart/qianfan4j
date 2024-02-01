package io.github.ompc.erniebot4j.test.function;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonClassDescription("the result of function call")
public record Result<T>(
        @JsonPropertyDescription("success or not")
        @JsonProperty("success")
        boolean isOk,

        @JsonPropertyDescription("message")
        String message,

        @JsonPropertyDescription("data")
        T data
) {

}
