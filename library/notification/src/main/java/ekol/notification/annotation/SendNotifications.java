package ekol.notification.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;

@Target({ElementType.METHOD}) 
@Retention(RUNTIME)
public @interface SendNotifications {
	public SendNotification[] value();
}
