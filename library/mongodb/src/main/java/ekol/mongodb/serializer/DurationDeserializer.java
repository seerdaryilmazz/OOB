package ekol.mongodb.serializer;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ekol.mongodb.domain.datetime.Duration;

import java.io.IOException;

/**
 * Created by burak on 08/02/17.
 */
public class DurationDeserializer  extends JsonDeserializer<Duration> {

    @Override
    public Duration deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        String value = jsonParser.getValueAsString();

        return new Duration(value);
    }
}
