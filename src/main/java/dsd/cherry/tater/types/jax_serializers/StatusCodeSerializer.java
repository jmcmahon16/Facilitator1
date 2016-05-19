package dsd.cherry.tater.types.jax_serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import dsd.cherry.tater.types.StatusCode;

import java.io.IOException;

/**
 * Created by James Beach on 5/4/2016.
 */
public class StatusCodeSerializer extends JsonSerializer<StatusCode> {
    @Override
    public void serialize(StatusCode code, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonProcessingException
    {
        System.out.println("appCode: " + code.getAppCode());
        System.out.println("message: " + code.getMessage());
        if (code == null) return;
        // gen.writeStartObject();
        gen.writeNumberField("appCode", code.getAppCode());
        gen.writeStringField("message", code.getMessage());
        // gen.writeEndObject();
    }
}
