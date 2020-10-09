package ekol.crm.activity.common;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FixedZoneDateTimeDeserializer extends JsonDeserializer<FixedZoneDateTime> {

    public static final DateTimeFormatter FORMATTER_WITH_TIMEZONE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm VV");

    @Override
    public FixedZoneDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        String value = jsonParser.getValueAsString();

        return deserialize(value);
    }

    public static FixedZoneDateTime deserialize(String str) {
        if (StringUtils.isNotBlank(str)) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(str, FORMATTER_WITH_TIMEZONE);
            return new FixedZoneDateTime(zonedDateTime.toInstant(), zonedDateTime.getZone().toString());
        } else {
            return null;
        }
    }
}
