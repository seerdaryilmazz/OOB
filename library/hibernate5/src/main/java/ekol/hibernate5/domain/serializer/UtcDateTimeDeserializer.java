package ekol.hibernate5.domain.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ekol.hibernate5.domain.embeddable.UtcDateTime;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class UtcDateTimeDeserializer extends JsonDeserializer<UtcDateTime> {

    private static final DateTimeFormatter FORMATTER_WITH_TIMEZONE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm VV");

    private static final ZoneId UTC = ZoneId.of("UTC");

    @Override
    public UtcDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        String value = jsonParser.getValueAsString();

        ZonedDateTime dateTimeAtUtc = ZonedDateTime.parse(value, FORMATTER_WITH_TIMEZONE).withZoneSameInstant(UTC);

        return new UtcDateTime(dateTimeAtUtc.toLocalDateTime());
    }
}
