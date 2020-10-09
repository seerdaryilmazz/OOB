package ekol.oneorder.configuration;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({ConfigurationApi.class, ConfigurationService.class})
public @interface OneOrderEnableConfiguration {

}
