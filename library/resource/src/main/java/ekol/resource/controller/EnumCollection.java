package ekol.resource.controller;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumCollection {
	public String[] packages() default {};
	public Class<?>[] enums() default {};
}
