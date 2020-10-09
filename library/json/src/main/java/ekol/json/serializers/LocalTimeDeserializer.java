package ekol.json.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        String val = p.getValueAsString();

        if (StringUtils.isNotBlank(val)) {
            return LocalTime.parse(val, FORMATTER);
        } else {
            return null;
        }
    }
}
