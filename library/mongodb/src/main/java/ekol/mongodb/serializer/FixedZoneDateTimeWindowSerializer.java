package ekol.mongodb.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import ekol.mongodb.domain.datetime.FixedZoneDateTimeWindow;

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

            if (object.getStart() == null) {
                jsonGenerator.writeNull();
            }else{
                //see FixedZoneDateTimeSerializer
                ZonedDateTime systemZoneDateTime = ZonedDateTime.of(object.getStart(), ZoneId.systemDefault());
                ZonedDateTime actualZoneDateTime = systemZoneDateTime.withZoneSameInstant(ZoneId.of(object.getTimeZone()));
                jsonGenerator.writeString(actualZoneDateTime.format(FORMATTER_WITH_TIMEZONE));
            }

            jsonGenerator.writeFieldName("end");

            if (object.getEnd() == null) {
                jsonGenerator.writeNull();
            } else {
                //see FixedZoneDateTimeSerializer
                ZonedDateTime systemZoneDateTime = ZonedDateTime.of(object.getEnd(), ZoneId.systemDefault());
                ZonedDateTime actualZoneDateTime = systemZoneDateTime.withZoneSameInstant(ZoneId.of(object.getTimeZone()));
                jsonGenerator.writeString(actualZoneDateTime.format(FORMATTER_WITH_TIMEZONE));

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
