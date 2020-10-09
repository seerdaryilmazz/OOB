package ekol.resource.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ekol.resource.json.annotation.EnumSerializableToJsonAsLookup;
import ekol.exceptions.ApplicationException;
import ekol.json.serializers.common.ConverterType;
import ekol.json.serializers.common.EnumJsonDeserializer;
import ekol.json.serializers.common.EnumJsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.lang.annotation.Annotation;
import java.util.*;

@Configuration
public class EnumsJacksonMessageConverterConfig extends WebMvcConfigurerAdapter {

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
            registerEnumsModule(jacksonMessageConverter);
            converters.add(jacksonMessageConverter);
        } else {
            registerEnumsModule(jacksonMessageConverter);
        }

        super.configureMessageConverters(converters);
    }

    private static void registerEnumsModule(MappingJackson2HttpMessageConverter jacksonMessageConverter) {

        ObjectMapper mapper = jacksonMessageConverter.getObjectMapper();

        SimpleModule module = new SimpleModule();

        for (Class type : getEnumsSerializableToJsonAsLookup()) {
            addSerializerAndDeserializer(module, type);
        }

        mapper.registerModule(module);
    }

    private static Set<Class> getEnumsSerializableToJsonAsLookup() {

        Set<Class> types = new HashSet<>();

        String scanPackage = "ekol";

        ClassPathScanningCandidateComponentProvider provider = createComponentScanner(EnumSerializableToJsonAsLookup.class);

        for (BeanDefinition beanDef : provider.findCandidateComponents(scanPackage)) {
            try {
                types.add(Class.forName(beanDef.getBeanClassName()));
            } catch (ClassNotFoundException e) {
                throw new ApplicationException("class not found", e);
            }
        }

        return types;
    }

    private static <T extends Annotation> ClassPathScanningCandidateComponentProvider createComponentScanner(Class<T> annotationType) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(annotationType));
        return provider;
    }

    private static <T extends Enum<T>> void addSerializerAndDeserializer(SimpleModule module, Class<T> type) {
        addSerializer(module, type);
        addDeserializer(module, type);
    }

    private static <T extends Enum<T>> void addSerializer(SimpleModule module, Class<T> type) {
        ConverterType converterType = type.getAnnotation(EnumSerializableToJsonAsLookup.class).value();

        Map<String, ConverterType> fieldConverterTypeMap = new HashMap<>();

        Arrays.stream(type.getFields()).forEach(field -> {
            if (field.getAnnotation(EnumSerializableToJsonAsLookup.class) != null) {
                fieldConverterTypeMap.put(field.getName(), field.getAnnotation(EnumSerializableToJsonAsLookup.class).value());
            }
        });

        module.addSerializer(type, new EnumJsonSerializer<T>(converterType, fieldConverterTypeMap));
    }

    private static <T extends Enum<T>> void addDeserializer(SimpleModule module, Class<T> type) {
        module.addDeserializer(type, new EnumJsonDeserializer<T>(type));
    }
}
