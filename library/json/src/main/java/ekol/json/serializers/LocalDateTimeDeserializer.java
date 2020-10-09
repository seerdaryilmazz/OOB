package ekol.json.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by kilimci on 10/06/16.
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String val = p.getValueAsString();
        if(StringUtils.isNotBlank(val)){
            return LocalDateTime.parse(val, FORMATTER);
        }
        return null;
    }
}
