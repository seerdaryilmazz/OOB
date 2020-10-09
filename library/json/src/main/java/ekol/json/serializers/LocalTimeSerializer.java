package ekol.json.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeSerializer extends JsonSerializer<LocalTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void serialize(LocalTime localTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeString(localTime.format(FORMATTER));
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType type) throws JsonMappingException {
        if (visitor != null) visitor.expectObjectFormat(type);
    }
}
