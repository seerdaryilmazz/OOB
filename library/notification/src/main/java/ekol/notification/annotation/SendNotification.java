package ekol.notification.annotation;

import java.lang.annotation.*;

import ekol.notification.dto.TargetType;

@Repeatable(SendNotifications.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SendNotification {
	public String concern();
	public String[] target() default {"result.createdBy"};
	public TargetType targetType() default TargetType.READ_FROM_DATA;
	public String beforeCondition() default "";
	public String afterCondition() default "";
}
