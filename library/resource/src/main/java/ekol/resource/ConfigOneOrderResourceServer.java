package ekol.resource;

import org.springframework.context.annotation.Import;

import ekol.resource.validation.ValidationAspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kilimci on 21/07/16.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ResourceServerConfiguration.class, MultipartResolverConfig.class, ValidationAspect.class})
public @interface ConfigOneOrderResourceServer {
}
