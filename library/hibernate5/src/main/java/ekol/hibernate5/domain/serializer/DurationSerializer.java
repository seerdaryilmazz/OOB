package ekol.hibernate5.domain.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import ekol.hibernate5.domain.embeddable.Duration;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Created by burak on 08/02/17.
 */
@Configuration
public class DurationSerializer extends JsonSerializer<Duration> {

    @Override
    public void serialize(Duration duration, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

        if (duration == null) {
            jsonGenerator.writeNull();
        } else {

            jsonGenerator.writeString(duration.getDurationAsString());

        }
    }

    /*
    *  Necessary for the schema generator.
    *  If not overridden, schema type will be "any"
    *  and OneOrderSchemaFactoryWrapper.expectObjectFormat will not be called
    */
    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType type) throws JsonMappingException {
        if (visitor != null) visitor.expectObjectFormat(type);
    }
}
