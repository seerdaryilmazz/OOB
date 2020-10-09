package ekol.crm.search.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import ekol.resource.oauth2.SessionOwner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Configuration
public class DateSerializer extends JsonSerializer<Date> implements ApplicationContextAware, ContextualSerializer {

    private String pattern;
    public DateSerializer(String pattern){
        this.pattern = pattern;
    }

    public DateSerializer(){
        this.pattern = "dd/MM/yyyy HH:mm";
    }

    @Autowired
    private static SessionOwner sessionOwner;

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (date == null) {
            jsonGenerator.writeNull();
        } else {
            ZoneId userTimeZone = ZoneId.of(sessionOwner.getCurrentUser().getTimeZoneId());
            ZonedDateTime dateTimeAtUserTimeZone = ZonedDateTime.ofInstant(date.toInstant(), userTimeZone);
            jsonGenerator.writeString(dateTimeAtUserTimeZone.format(DateTimeFormatter.ofPattern(pattern)));
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        sessionOwner = (SessionOwner)applicationContext.getBean(SessionOwner.class);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        JsonDatePattern annotation = beanProperty.getAnnotation(JsonDatePattern.class);
        if(null != annotation){
            return new DateSerializer(annotation.value());
        }
        return new DateSerializer();
    }
}

