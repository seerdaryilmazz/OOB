package ekol.hibernate5.domain.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import ekol.resource.oauth2.SessionOwner;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class UtcDateTimeSerializer extends JsonSerializer<UtcDateTime> implements ApplicationContextAware {

    private static final DateTimeFormatter FORMATTER_WITH_TIMEZONE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm VV");

    private static final ZoneId UTC = ZoneId.of("UTC");

    private static SessionOwner sessionOwner;

    public UtcDateTimeSerializer() {

    }

    @Override
    public void serialize(UtcDateTime object, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

        if (object == null) {
            jsonGenerator.writeNull();
        } else {

            ZoneId userTimeZone = ZoneId.of(sessionOwner.getCurrentUser().getTimeZoneId());

            ZonedDateTime dateTimeAtUserTimeZone = ZonedDateTime.of(object.getDateTime(), UTC).withZoneSameInstant(userTimeZone);

            jsonGenerator.writeString(dateTimeAtUserTimeZone.format(FORMATTER_WITH_TIMEZONE));
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        UtcDateTimeSerializer.sessionOwner = applicationContext.getBean(SessionOwner.class);
    }
}
