package ekol.orders.order.domain.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeSerializer extends JsonSerializer<ZonedDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm VV");

    @Override
    public void serialize(ZonedDateTime zonedDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeString(zonedDateTime.format(FORMATTER));
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType type) throws JsonMappingException {
        if (visitor != null) visitor.expectObjectFormat(type);
    }
}
