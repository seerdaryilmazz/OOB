package ekol.crm.search.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateDeserializer extends JsonDeserializer<Date> implements ContextualDeserializer {

    private String pattern;
    public DateDeserializer(String pattern){
        this.pattern = pattern;
    }

    public DateDeserializer(){
        this.pattern = "dd/MM/yyyy HH:mm";
    }


    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String val = p.getValueAsString();
        if(StringUtils.isNotBlank(val)){
            try{

            return new SimpleDateFormat(pattern).parse(val);

            } catch (ParseException  e){
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) throws JsonMappingException {
        JsonDatePattern annotation = beanProperty.getAnnotation(JsonDatePattern.class);
        if(null != annotation){
            return new DateDeserializer(annotation.value());
        }
        return new DateDeserializer();
    }
}
