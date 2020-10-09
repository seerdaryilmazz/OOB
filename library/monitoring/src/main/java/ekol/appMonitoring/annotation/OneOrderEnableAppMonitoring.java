package ekol.appMonitoring.annotation;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

import ekol.appMonitoring.MonitoringConfiguration;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({MonitoringConfiguration.class})
public @interface OneOrderEnableAppMonitoring {
}
