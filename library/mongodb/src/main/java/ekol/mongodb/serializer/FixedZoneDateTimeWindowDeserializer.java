package ekol.mongodb.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import ekol.mongodb.domain.datetime.FixedZoneDateTime;
import ekol.mongodb.domain.datetime.FixedZoneDateTimeWindow;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
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
        }
        if (node.hasNonNull("end")) {
            endAsText = node.get("end").asText();
        }

        FixedZoneDateTime start = null;
        FixedZoneDateTime end = null;
        if (StringUtils.isNotBlank(startAsText)) {
            start = FixedZoneDateTime.parse(startAsText);
        }
        if (StringUtils.isNotBlank(endAsText)) {
            end = FixedZoneDateTime.parse(endAsText);
        }
        if (start != null || end != null) {
            return new FixedZoneDateTimeWindow(start, end);
        } else {
            return null;
        }
    }
}
