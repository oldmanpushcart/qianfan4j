package io.github.oldmanpushcart.internal.qianfan4j.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.oldmanpushcart.internal.qianfan4j.util.JacksonUtils;
import io.github.oldmanpushcart.internal.qianfan4j.util.StringUtils;
import io.github.oldmanpushcart.qianfan4j.chat.FunctionCall;
import io.github.oldmanpushcart.qianfan4j.chat.function.*;
import io.github.oldmanpushcart.qianfan4j.chat.message.Message;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Stream;

import static io.github.oldmanpushcart.internal.qianfan4j.util.StringUtils.isNotBlank;
import static io.github.oldmanpushcart.internal.qianfan4j.util.StringUtils.toSnake;

public record ChatFunctionMeta(
        @JsonProperty("name")
        String name,
        @JsonProperty("description")
        String description,
        @JsonProperty("parameters")
        Schema parameter,
        @JsonProperty("responses")
        Schema response,
        @JsonProperty("examples")
        List<Example> examples
) {

    private static final ObjectMapper mapper = JacksonUtils.mapper();

    public record Schema(Type type) {

        @JsonValue
        JsonNode extract() {
            return JacksonUtils.schema(mapper, type);
        }

    }

    public record Example(String question, FunctionCall call) {

        @JsonValue
        Message[] extract() {
            return new Message[]{
                    Message.ofUser(question),
                    Message.ofFunctionCall(call)
            };
        }

    }

    public static ChatFunctionMeta of(Class<?> functionClass) {

        // 检查是否实现ChatFunction接口
        if (!ChatFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("required implements interface: %s".formatted(
                    ChatFunction.class.getName()
            ));
        }

        // 检查是否有注解
        if (!functionClass.isAnnotationPresent(ChatFn.class)) {
            throw new IllegalArgumentException("required annotation: %s".formatted(
                    ChatFn.class.getName()
            ));
        }

        // 找到ChatFunction接口
        final var interfaceType = Stream.of(functionClass.getGenericInterfaces())
                .filter(genericInterface -> genericInterface instanceof ParameterizedType)
                .map(genericInterface -> (ParameterizedType) genericInterface)
                .filter(pType -> pType.getRawType().equals(ChatFunction.class))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("required implements interface: %s".formatted(
                        ChatFunction.class.getName()
                )));

        final var anChatFn = functionClass.getAnnotation(ChatFn.class);
        final var name = isNotBlank(anChatFn.name()) ? anChatFn.name() : toSnake(functionClass.getSimpleName());
        final var parameterType = interfaceType.getActualTypeArguments()[0];
        final var returnType = interfaceType.getActualTypeArguments()[1];
        final var examples = Stream.of(functionClass.getAnnotations())
                .flatMap(annotation -> {
                    if (annotation instanceof ChatFnExample anExample) {
                        return Stream.of(anExample);
                    } else if (annotation instanceof ChatFnExamples anExamples) {
                        return Stream.of(anExamples.value());
                    }
                    return Stream.empty();
                })
                .map(anExample -> anExample.negative()
                        ? new Example(anExample.question(), new FunctionCall("", "{}", "我无需调用任何工具"))
                        : new Example(anExample.question(), new FunctionCall(name, anExample.arguments(), anExample.thoughts()))
                )
                .toList();

        // schema
        return new ChatFunctionMeta(
                name,
                anChatFn.description(),
                new Schema(parameterType),
                new Schema(returnType),
                examples
        );

    }

}
