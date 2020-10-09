package ekol.crm.activity.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FixedZoneDateTimeSerializer extends JsonSerializer<FixedZoneDateTime> {

    public static final DateTimeFormatter FORMATTER_WITH_TIMEZONE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm VV");

    @Override
    public void serialize(FixedZoneDateTime object, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

        String str = serialize(object);

        if (str == null) {
            jsonGenerator.writeNull();
        } else {
            jsonGenerator.writeString(str);
        }
    }

    public static String serialize(FixedZoneDateTime object) {
        if (object == null) {
            return null;
        } else {
            return object.getDateTimeUtc().atZone(ZoneId.of(object.getTimeZone())).format(FORMATTER_WITH_TIMEZONE);
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
