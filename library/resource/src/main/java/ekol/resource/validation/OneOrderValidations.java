package ekol.resource.validation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

@Target({ElementType.METHOD}) 
@Retention(RUNTIME) 
public @interface OneOrderValidations {
	public OneOrderValidation[] value();
}
