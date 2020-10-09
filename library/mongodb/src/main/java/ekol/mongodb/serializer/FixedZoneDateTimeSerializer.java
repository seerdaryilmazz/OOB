package ekol.mongodb.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import ekol.mongodb.domain.datetime.FixedZoneDateTime;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FixedZoneDateTimeSerializer extends JsonSerializer<FixedZoneDateTime> {

    private static final DateTimeFormatter FORMATTER_WITH_TIMEZONE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm VV");

    @Override
    public void serialize(FixedZoneDateTime object, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

        if (object == null) {
            jsonGenerator.writeNull();
        } else {
            //e.g. mongo has 13:00 UTC written, when we access it, it becomes 16:00 Istanbul
            //so we have to construct it with system default tz
            ZonedDateTime systemZoneDateTime = ZonedDateTime.of(object.getDateTime(), ZoneId.systemDefault());
            //then convert it to written tz
            ZonedDateTime actualZoneDateTime = systemZoneDateTime.withZoneSameInstant(ZoneId.of(object.getTimeZone()));
            jsonGenerator.writeString(actualZoneDateTime.format(FORMATTER_WITH_TIMEZONE));
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
