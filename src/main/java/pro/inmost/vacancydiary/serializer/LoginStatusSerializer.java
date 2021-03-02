package pro.inmost.vacancydiary.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import pro.inmost.vacancydiary.status.LoginStatus;

public class LoginStatusSerializer extends JsonSerializer<LoginStatus> {

    @Override
    public void serialize(LoginStatus loginStatus, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {
        String fieldName = "status";

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField(fieldName, LoginStatus.SUCCESS.name());
            jsonGenerator.writeEndObject();
    }
}
