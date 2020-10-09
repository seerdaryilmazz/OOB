package ekol.json.schema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

/**
 * Created by ozer on 20/01/2017.
 */
public class JsonSchemaUtils {

    public static JsonSchema createSchemaFor(String className) {
        return createSchemaFor(className, new ObjectMapper());
    }

    public static JsonSchema createSchemaFor(String className, ObjectMapper objectMapper) {
        SchemaFactoryWrapper visitor = new OneOrderSchemaFactoryWrapper();

        try {
            objectMapper.acceptJsonFormatVisitor(objectMapper.constructType(Class.forName(className)), visitor);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        return visitor.finalSchema();
    }
}
