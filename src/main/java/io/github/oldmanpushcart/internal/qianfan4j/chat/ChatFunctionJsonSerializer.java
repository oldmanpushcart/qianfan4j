package io.github.oldmanpushcart.internal.qianfan4j.chat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.oldmanpushcart.qianfan4j.chat.function.ChatFunction;

import java.io.IOException;

public class ChatFunctionJsonSerializer extends JsonSerializer<ChatFunction<?, ?>> {

    @Override
    public void serialize(ChatFunction<?, ?> function, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeObject(ChatFunctionMeta.of(function.getClass()));
    }

}
