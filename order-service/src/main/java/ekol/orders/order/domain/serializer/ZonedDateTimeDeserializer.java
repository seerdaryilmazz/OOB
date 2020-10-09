package ekol.orders.order.domain.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm VV");

    @Override
    public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String val = p.getValueAsString();
        if(StringUtils.isNotBlank(val)){
            return ZonedDateTime.parse(val, FORMATTER);
        }
        return null;
    }
}
