package ekol.notification.annotation;

import java.lang.annotation.*;

import org.springframework.context.annotation.Import;

import ekol.notification.api.NotificationApi;
import ekol.notification.aspect.NotificationSender;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({NotificationApi.class, NotificationSender.class})
public @interface OneOrderEnableNotification {

}
