package ekol.mongodb.serializer;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import ekol.mongodb.domain.datetime.UtcDateTime;
import ekol.resource.oauth2.SessionOwner;

@Configuration
public class UtcDateTimeSerializer extends JsonSerializer<UtcDateTime> implements ApplicationContextAware {

    private static final DateTimeFormatter FORMATTER_WITH_TIMEZONE = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm VV");
    private static SessionOwner sessionOwner;

    @Override
    public void serialize(UtcDateTime object, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

    	if (object == null) {
            jsonGenerator.writeNull();
        } else {
            ZoneId userTimeZone = ZoneId.of(sessionOwner.getCurrentUser().getTimeZoneId());
            ZonedDateTime dateTimeAtUserTimeZone = ZonedDateTime.of(object.getDateTime(), ZoneId.systemDefault()).withZoneSameInstant(userTimeZone);
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
