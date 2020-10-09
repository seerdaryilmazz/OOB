package ekol.resource.json.annotation;

import ekol.json.annotation.CustomSchema;
import ekol.json.annotation.CustomSchemaType;
import ekol.json.serializers.common.ConverterType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * LookupValueLabel şeklinde serialize ve deserialize edilecek enum'lar bu annotation ile
 * işaretlenmelidir.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
@CustomSchema(CustomSchemaType.ENUM)
public @interface EnumSerializableToJsonAsLookup {

    ConverterType value() default ConverterType.NONE;
}
