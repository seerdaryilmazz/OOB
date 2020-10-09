package ekol.resource.validation;

import java.lang.annotation.*;

@Repeatable(OneOrderValidations.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OneOrderValidation {
	Class<? extends Validator> value();
}
