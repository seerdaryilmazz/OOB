package ekol.resource.json.annotation;

import ekol.resource.json.ContentNegotiationConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kilimci on 25/07/2017.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ContentNegotiationConfig.class})
public @interface ConfigOneOrderContentNegotiation {
}
