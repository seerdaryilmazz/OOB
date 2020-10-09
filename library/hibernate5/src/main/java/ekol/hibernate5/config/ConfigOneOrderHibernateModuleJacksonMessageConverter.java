package ekol.hibernate5.config;


import ekol.resource.json.MappingJackson2HttpMessageConverterConfig;
import ekol.hibernate5.domain.serializer.UtcDateTimeSerializer;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({MappingJackson2HttpMessageConverterConfig.class, HibernateModuleJacksonMessageConverterConfig.class, UtcDateTimeSerializer.class})
public @interface ConfigOneOrderHibernateModuleJacksonMessageConverter {
}