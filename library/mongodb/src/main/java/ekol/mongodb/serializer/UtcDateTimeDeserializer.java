package ekol.mongodb.serializer;


import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import ekol.mongodb.domain.datetime.UtcDateTime;

public class UtcDateTimeDeserializer extends JsonDeserializer<UtcDateTime> {

    private static final DateTimeFormatter FORMATTER_WITH_TIMEZONE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm VV");

    @Override
    public UtcDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        String value = jsonParser.getValueAsString();
        if(StringUtils.isNotBlank(value)){
            return UtcDateTime.withZonedTime(ZonedDateTime.parse(value, FORMATTER_WITH_TIMEZONE).withZoneSameInstant(ZoneId.systemDefault()));
        }
        return null;
    }
}
