package ekol.resource.json.annotation;

import ekol.resource.json.JavaTypesJacksonMessageConverterConfig;
import ekol.resource.json.MappingJackson2HttpMessageConverterConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({MappingJackson2HttpMessageConverterConfig.class, JavaTypesJacksonMessageConverterConfig.class})
public @interface ConfigOneOrderJavaTypesJacksonMessageConverter {
}
