package ekol.hibernate5.domain.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FixedZoneDateTimeDeserializer extends JsonDeserializer<FixedZoneDateTime> {

    private static final DateTimeFormatter FORMATTER_WITH_TIMEZONE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm VV");

    @Override
    public FixedZoneDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        String value = jsonParser.getValueAsString();
        if(StringUtils.isBlank(value)){
            return null;
        }
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(value, FORMATTER_WITH_TIMEZONE);
        return new FixedZoneDateTime(zonedDateTime.toLocalDateTime(), zonedDateTime.getZone().getId());
    }
}
