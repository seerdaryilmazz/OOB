package ekol.mongodb.serializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ekol.mongodb.domain.datetime.FixedZoneDateTime;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class FixedZoneDateTimeDeserializer extends JsonDeserializer<FixedZoneDateTime> {

    private static final DateTimeFormatter FORMATTER_WITH_TIMEZONE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm VV");

    @Override
    public FixedZoneDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        String value = jsonParser.getValueAsString();
        if(StringUtils.isNotBlank(value)){
            return FixedZoneDateTime.parse(value);
        }
        return null;

    }
}
