package io.github.ompc.erniebot4j.chat.codec;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.ompc.erniebot4j.chat.message.Message;

import java.io.IOException;

public class MessageRoleJsonSerializer extends JsonSerializer<Message.Role> {

    @Override
    public void serialize(Message.Role role, JsonGenerator generator, SerializerProvider provider) throws IOException {
        switch (role) {
            case BOT:
                generator.writeString("assistant");
                break;
            case HUMAN:
                generator.writeString("user");
                break;
            case FUNCTION:
                generator.writeString("function");
                break;
            default:
                throw new IllegalArgumentException("unknown role: " + role);
        }
    }

}
