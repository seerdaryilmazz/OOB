package ekol.crm.search.serializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.METHOD })
public @interface JsonDatePattern {
    String value() default "dd/MM/yyyy HH:mm";
}
