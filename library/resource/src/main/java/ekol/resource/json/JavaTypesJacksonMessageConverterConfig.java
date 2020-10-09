package ekol.resource.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ekol.json.serializers.*;
import ekol.json.serializers.common.ConverterType;
import ekol.json.serializers.common.EnumJsonDeserializer;
import ekol.json.serializers.common.EnumJsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Configuration
public class JavaTypesJacksonMessageConverterConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        MappingJackson2HttpMessageConverter jacksonMessageConverter = null;

        if (converters != null) {
            for (HttpMessageConverter<?> converter : converters) {
                if (converter instanceof MappingJackson2HttpMessageConverter) {
                    jacksonMessageConverter = (MappingJackson2HttpMessageConverter) converter;
                    break;
                }
            }
        }

        if (jacksonMessageConverter == null) {
            jacksonMessageConverter = mappingJackson2HttpMessageConverter;
            registerJavaTypesModule(jacksonMessageConverter);
            converters.add(jacksonMessageConverter);
        } else {
            registerJavaTypesModule(jacksonMessageConverter);
        }

        super.configureMessageConverters(converters);
    }

    private static void registerJavaTypesModule(MappingJackson2HttpMessageConverter jacksonMessageConverter) {

        ObjectMapper mapper = jacksonMessageConverter.getObjectMapper();

        SimpleModule module = new SimpleModule();

        module.addSerializer(LocalDate.class, new LocalDateSerializer());
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer());

        module.addSerializer(LocalTime.class, new LocalTimeSerializer());
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer());

        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        module.addSerializer(DayOfWeek.class, new EnumJsonSerializer<>(ConverterType.INITIAL_CASE, null));
        module.addDeserializer(DayOfWeek.class, new EnumJsonDeserializer<>(DayOfWeek.class));

        mapper.registerModule(module);
    }
}
