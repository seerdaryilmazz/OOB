package ekol.hibernate5.domain.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTimeWindow;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FixedZoneDateTimeWindowSerializer extends JsonSerializer<FixedZoneDateTimeWindow> {

    private static final DateTimeFormatter FORMATTER_WITH_TIMEZONE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm VV");

    @Override
    public void serialize(FixedZoneDateTimeWindow object, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

        if (object == null) {
            jsonGenerator.writeNull();
        } else {

            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("start");

            if (object.getStart() != null) {
                jsonGenerator.writeString(ZonedDateTime.of(object.getStart(), ZoneId.of(object.getTimeZone())).format(FORMATTER_WITH_TIMEZONE));
            } else {
                jsonGenerator.writeNull();
            }

            jsonGenerator.writeFieldName("end");

            if (object.getEnd() != null) {
                jsonGenerator.writeString(ZonedDateTime.of(object.getEnd(), ZoneId.of(object.getTimeZone())).format(FORMATTER_WITH_TIMEZONE));
            } else {
                jsonGenerator.writeNull();
            }

            jsonGenerator.writeEndObject();
        }
    }

    /*
    *  Necessary for the schema generator.
    *  If not overridden, schema type will be "any"
    *  and OneOrderSchemaFactoryWrapper.expectObjectFormat will not be called
    */
    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType type) throws JsonMappingException {
        if (visitor != null) visitor.expectObjectFormat(type);
    }
}
