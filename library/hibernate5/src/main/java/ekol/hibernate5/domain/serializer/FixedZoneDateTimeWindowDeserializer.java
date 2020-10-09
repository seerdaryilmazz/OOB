package ekol.hibernate5.domain.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTime;
import ekol.hibernate5.domain.embeddable.FixedZoneDateTimeWindow;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FixedZoneDateTimeWindowDeserializer extends JsonDeserializer<FixedZoneDateTimeWindow> {

    private static final DateTimeFormatter FORMATTER_WITH_TIMEZONE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm VV");

    @Override
    public FixedZoneDateTimeWindow deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String startAsText = null;
        String endAsText = null;

        if (node.hasNonNull("start")) {
            startAsText = node.get("start").asText();
            // boş string geldiğinde hata oluşmasını engellemek için...
            if (startAsText.length() == 0) {
                startAsText = null;
            }
        }

        if (node.hasNonNull("end")) {
            endAsText = node.get("end").asText();
            // boş string geldiğinde hata oluşmasını engellemek için...
            if (endAsText.length() == 0) {
                endAsText = null;
            }
        }

        FixedZoneDateTime start = null;
        FixedZoneDateTime end = null;

        if (startAsText != null) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(startAsText, FORMATTER_WITH_TIMEZONE);
            start = new FixedZoneDateTime(zonedDateTime.toLocalDateTime(), zonedDateTime.getZone().getId());
        }

        if (endAsText != null) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(endAsText, FORMATTER_WITH_TIMEZONE);
            end = new FixedZoneDateTime(zonedDateTime.toLocalDateTime(), zonedDateTime.getZone().getId());
        }

        if (start != null || end != null) {
            return new FixedZoneDateTimeWindow(start, end);
        } else {
            return null;
        }
    }
}
