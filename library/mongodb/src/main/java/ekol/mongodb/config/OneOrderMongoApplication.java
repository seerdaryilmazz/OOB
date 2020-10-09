package ekol.mongodb.config;

import ekol.mongodb.serializer.UtcDateTimeSerializer;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kilimci on 02/03/2017.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({AuditConfig.class, UtcDateTimeSerializer.class})
public @interface OneOrderMongoApplication {
}


