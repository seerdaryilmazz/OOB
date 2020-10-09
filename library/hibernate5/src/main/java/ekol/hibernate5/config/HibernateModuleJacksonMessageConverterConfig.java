package ekol.hibernate5.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class HibernateModuleJacksonMessageConverterConfig extends WebMvcConfigurerAdapter {

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
            registerHibernate5Module(jacksonMessageConverter);
            converters.add(jacksonMessageConverter);
        } else {
            registerHibernate5Module(jacksonMessageConverter);
        }

        super.configureMessageConverters(converters);
    }

    /**
     * Hibernate5Module'ün temel olarak çözüm bulduğu sorun şudur: Bir entity'nin içinde lazy referanslar varsa,
     * Hibernate bu lazy referanslar için proxy objeleri oluşturacaktır. Json serialization aşamasında ise Jackson,
     * bu proxy objelerini de serialize etmeye çalışacaktır. Ancak bu proxy objeleri için serializer bulunmayacağı için
     * şu şekilde bir hata oluşacaktır:
     *
     * Failed to write HTTP message: org.springframework.http.converter.HttpMessageNotWritableException:
     * Could not write content: No serializer found for class org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer and
     * no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS)
     *
     * NOT: EntityGraph kullanan sorgu sonuçlarında bu hata ortaya çıkmamaktadır.
     */
    private static void registerHibernate5Module(MappingJackson2HttpMessageConverter jacksonMessageConverter) {

        ObjectMapper mapper = jacksonMessageConverter.getObjectMapper();

        Hibernate5Module module = new Hibernate5Module();

        // If USE_TRANSIENT_ANNOTATION is enabled,
        // properties which are annotated with javax.persistence.Transient are ignored.
        module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);

        // Registering Hibernate5Module to support lazy objects
        mapper.registerModule(module);
    }
}