package ekol.hibernate5.test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import ekol.exceptions.ApplicationException;
import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.test.BuilderClassAndEntityClassCompatibility;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ozer on 31/01/2017.
 */
public class TestUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);

    private static List<Class> findBuilders() {
        try {
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

            List<Class> candidates = new ArrayList<>();
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "ekol/**/*Builder.class";
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    Class clazz = Class.forName(metadataReader.getClassMetadata().getClassName());
                    LOGGER.info("Found builder class : " + clazz.getName());
                    candidates.add(clazz);
                }
            }
            return candidates;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T extends BaseEntity> void softDeleteAllOneRepository(ApplicationRepository<T> repository) {
        Iterable<T> iterable = repository.findAll();

        for (T entity : iterable) {
            entity.setDeleted(true);
            repository.save(entity);
        }
    }

    private static Map<Class<?>, Class<?>> findBuilderClassesAndCorrespondingEntityClasses() {
        Map<Class<?>, Class<?>> builderClassVersusEntityClass = new HashMap<>();

        List<Class> builderClasses = findBuilders();
        for (Class builderClass : builderClasses) {
            Class<?> entityClass = null;

            for (Method builderClassMethod : builderClass.getMethods()) {
                if (builderClassMethod.getName().equals("build")
                        && builderClassMethod.getParameterCount() == 0
                        && Modifier.isPublic(builderClassMethod.getModifiers())
                        && !builderClassMethod.getReturnType().equals(Void.TYPE)) {
                    entityClass = builderClassMethod.getReturnType();
                    break;
                }
            }

            if (entityClass == null) {
                throw new ApplicationException("Could not determine the entity class for " + builderClass.getName() + ".");
            } else {
                builderClassVersusEntityClass.put(builderClass, entityClass);
                LOGGER.info("Found entity class : " + entityClass.getName() + " for builder class " + builderClass.getName());
            }
        }

        return builderClassVersusEntityClass;
    }

    private static void checkBuilderClassAndEntityClassCompatibility(Class<?> builderClass, Class<?> entityClass) {
        BuilderClassAndEntityClassCompatibility compatibility = BuilderClassAndEntityClassCompatibility.check(builderClass, entityClass);

        StringBuilder msg = new StringBuilder();
        if (!compatibility.isCompatible()) {
            msg.append("Builder class and entity class must be compatible. See details below.");

            if (compatibility.getMissingMethodsInBuilderClass().size() > 0) {
                msg.append("\n");
                msg.append("\t" + "Missing methods in builder class '" + builderClass.getName() + "':");

                for (String s : compatibility.getMissingMethodsInBuilderClass()) {
                    msg.append("\n");
                    msg.append("\t\t" + s);
                }
            }

            if (compatibility.getMissingPropertiesInEntityClass().size() > 0) {
                msg.append("\n");
                msg.append("\t" + "Missing properties in entity class '" + entityClass.getName() + "':");

                for (String s : compatibility.getMissingPropertiesInEntityClass()) {
                    msg.append("\n");
                    msg.append("\t\t" + s);
                }
            }
        }

        Assert.assertTrue(msg.toString(), compatibility.isCompatible());
    }

    public static void checkBuilderClassAndEntityClassCompatibility() {
        Map<Class<?>, Class<?>> map = findBuilderClassesAndCorrespondingEntityClasses();

        for (Class<?> builderClass : map.keySet()) {
            checkBuilderClassAndEntityClassCompatibility(builderClass, map.get(builderClass));
        }
    }

    public static void softDeleteAll(ApplicationRepository<?>... repositories) {
        for (ApplicationRepository<?> repository : repositories) {
            softDeleteAllOneRepository((ApplicationRepository<? extends BaseEntity>) repository);
        }
    }

    public static <T> String serializeObject(JsonSerializer<T> serializer, T object) {
        try {
            Writer jsonWriter = new StringWriter();
            JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
            SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
            serializer.serialize(object, jsonGenerator, serializerProvider);
            jsonGenerator.flush();
            return jsonWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserializeObject(JsonDeserializer<T> deserializer, String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
            JsonParser parser = mapper.getFactory().createParser(stream);
            DeserializationContext ctx = mapper.getDeserializationContext();
            return deserializer.deserialize(parser, ctx);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
