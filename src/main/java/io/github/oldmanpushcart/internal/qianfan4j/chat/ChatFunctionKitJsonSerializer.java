package io.github.oldmanpushcart.internal.qianfan4j.chat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.oldmanpushcart.qianfan4j.chat.function.ChatFunctionKit;

import java.io.IOException;

public class ChatFunctionKitJsonSerializer extends JsonSerializer<ChatFunctionKit> {

    @Override
    public void serialize(ChatFunctionKit kit, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeStartArray();
        for (var stub : kit) {
            generator.writeObject(stub.meta());
        }
        generator.writeEndArray();
    }

}
