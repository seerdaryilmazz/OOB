package ekol.event.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ozer on 26/10/16.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProducesEvent {
    String event();
    int delay() default 0;
    boolean transactional() default false;
    boolean logMessage() default true;
}

