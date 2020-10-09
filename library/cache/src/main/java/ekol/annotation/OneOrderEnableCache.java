package ekol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;

import ekol.config.RedisCacheConfiguration;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EnableCaching
@Import(RedisCacheConfiguration.class)
public @interface OneOrderEnableCache {

}
