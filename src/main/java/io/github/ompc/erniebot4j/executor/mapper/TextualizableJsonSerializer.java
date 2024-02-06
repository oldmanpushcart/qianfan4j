package io.github.ompc.erniebot4j.executor.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.ompc.erniebot4j.executor.Textualizable;

import java.io.IOException;

public class TextualizableJsonSerializer extends JsonSerializer<Textualizable> {

    @Override
    public void serialize(Textualizable textualizable, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeString(textualizable.getText());
    }

}
