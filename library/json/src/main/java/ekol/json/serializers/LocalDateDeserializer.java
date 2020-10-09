package ekol.json.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by kilimci on 13/04/16.
 */
public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String val = p.getValueAsString();
        if(StringUtils.isNotBlank(val)){
            return LocalDate.parse(val, FORMATTER);
        }
        return null;
    }
}
