package ekol.json.schema;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.module.jsonSchema.factories.ObjectVisitor;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;
import com.fasterxml.jackson.module.jsonSchema.types.StringSchema;
import ekol.json.annotation.CustomSchema;
import ekol.json.annotation.CustomSchemaType;
import org.springframework.core.annotation.AnnotationUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by ozer on 20/01/2017.
 */
public class OneOrderSchemaFactoryWrapper extends SchemaFactoryWrapper {

    public OneOrderSchemaFactoryWrapper() {
        super(null, new OneOrderWrapperFactory());
    }

    public OneOrderSchemaFactoryWrapper(SerializerProvider p) {
        super(p, new OneOrderWrapperFactory());
    }

    private CustomSchemaType getCustomSchemaType(Class clazz) {
        CustomSchema customSchema = AnnotationUtils.findAnnotation(clazz, CustomSchema.class);
        return customSchema != null ? customSchema.value() : null;
    }

    @Override
    public JsonObjectFormatVisitor expectObjectFormat(JavaType convertedType) {
        CustomSchemaType customSchemaType = null;

        if (convertedType.getRawClass().getName().equals(LocalDate.class.getName())) {
            customSchemaType = CustomSchemaType.LOCAL_DATE;
        } else if (convertedType.getRawClass().getName().equals(LocalDateTime.class.getName())) {
            customSchemaType = CustomSchemaType.LOCAL_DATE_TIME;
        } else {
            customSchemaType = getCustomSchemaType(convertedType.getRawClass());
        }

        ObjectSchema s = customSchemaType != null
                ? new CustomObjectSchema(customSchemaType)
                : schemaProvider.objectSchema();

        schema = s;

        if (visitorContext == null) {
            visitorContext = new OneOrderVisitorContext();
        }

        String schemaUri = visitorContext.addSeenSchemaUri(convertedType);
        if (schemaUri != null) {
            s.setId(schemaUri);
        }

        ObjectVisitor visitor = (ObjectVisitor) visitorFactory.objectFormatVisitor(provider, s);
        visitor.setVisitorContext(visitorContext);
        return visitor;
    }

    @Override
    public JsonStringFormatVisitor expectStringFormat(JavaType convertedType) {
        CustomSchemaType customSchemaType = getCustomSchemaType(convertedType.getRawClass());
        StringSchema s = customSchemaType == CustomSchemaType.ENUM
                ? new CustomEnumStringSchema(customSchemaType)
                : schemaProvider.stringSchema();

        schema = s;
        return visitorFactory.stringFormatVisitor(s);
    }
}
