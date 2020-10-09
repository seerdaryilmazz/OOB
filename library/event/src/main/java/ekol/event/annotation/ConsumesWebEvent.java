package ekol.event.annotation;

import java.lang.annotation.*;

/**
 * Created by kilimci on 11/08/2017.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsumesWebEvent {
    String event();
    String name();
    boolean delayed() default false;
    boolean transactional() default false;
    ConsumesWebEventParam[] params() default {};
}
