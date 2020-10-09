package ekol.notification.aspect;

import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import ekol.notification.annotation.SendNotification;
import ekol.notification.api.NotificationApi;
import ekol.notification.dto.*;
import lombok.AllArgsConstructor;

@Aspect
@Component
@ComponentScan
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationSender {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationSender.class);

	private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();
	
	private NotificationApi notificationApi;
	private ApplicationContext applicationContext;
	
	@Around("ekol.notification.aspect.NotificationSender.sendNotification() || ekol.notification.aspect.NotificationSender.sendNotifications()")
	public Object aroundSendNotifications(ProceedingJoinPoint call) throws Throwable {

		SendNotification[] notifications = ((MethodSignature) call.getSignature()).getMethod().getAnnotationsByType(SendNotification.class);
		
		Boolean[] beforeConditionResults = new Boolean[notifications.length];
		Boolean[] afterConditionResults = new Boolean[notifications.length];
		
		final EvaluationContext contextBefore = getEvaluationContext(NotificationContext.with(call.getArgs()));
		IntStream.range(0, notifications.length).forEach(i->{
			SendNotification notification = notifications[i];

			boolean beforeConditionResult = true;
			if(StringUtils.isNotBlank(notification.beforeCondition())) {
				beforeConditionResult = evaluateExpression(contextBefore, notification.beforeCondition(), Boolean.class);
			}
			beforeConditionResults[i] = beforeConditionResult;
		});
		
		Object retVal = call.proceed();
		
		if(Objects.nonNull(retVal)) {
			final EvaluationContext contextAfter = getEvaluationContext(NotificationContext.with(call.getArgs(), retVal));
			
			IntStream.range(0, notifications.length).forEach(i->{
				SendNotification notification = notifications[i];

				boolean afterConditionResult = true;
				if(StringUtils.isNotBlank(notification.afterCondition())) {
					afterConditionResult = evaluateExpression(contextAfter, notification.afterCondition(), Boolean.class);
				}
				afterConditionResults[i] = afterConditionResult;
			});
			IntStream.range(0, notifications.length).forEach(i->{
				if(Optional.ofNullable(beforeConditionResults[i]).orElse(Boolean.FALSE) && Optional.ofNullable(afterConditionResults[i]).orElse(Boolean.FALSE)) {
					this.sendNotification(notifications[i], contextAfter);
				}
			});
		}
		return retVal;
	}
	
	private EvaluationContext getEvaluationContext(NotificationContext notificationContext) {
		StandardEvaluationContext context = new StandardEvaluationContext(notificationContext);
		context.setBeanResolver(new BeanFactoryResolver(applicationContext));
		return context;
	}
	
	private synchronized <T> T evaluateExpression(EvaluationContext context, String expression, Class<T> returnType){
		try {
			return EXPRESSION_PARSER.parseExpression(expression).getValue(context, returnType);
		} catch(Exception e) {
			LOGGER.error("Notification expression parse ERROR", e);
		}
		return null;
	}
	
	private void sendNotification(SendNotification notification, EvaluationContext context) {
		try {
			NotificationContext notificationContext = (NotificationContext)context.getRootObject().getValue();
			if(TargetType.READ_FROM_DATA == notification.targetType()) {
				Stream.of(notification.target())
					.map(target->evaluateExpression(context, target, String.class))
					.forEach(target->{
						if(NumberUtils.isDigits(target)) {
							notificationApi.sendNotification(notification.concern(), notificationContext.getResult(), Long.valueOf(target));
						} else {
							notificationApi.sendNotification(notification.concern(), notificationContext.getResult(), target);
						}
					});
			} else if(TargetType.USERNAME == notification.targetType()) {
				notificationApi.sendNotification(notification.concern(), notificationContext.getResult(), notification.target());
			} else if(TargetType.USER_ID == notification.targetType()) {
				notificationApi.sendNotification(notification.concern(), notificationContext.getResult(), Stream.of(notification.target()).map(Long::valueOf).toArray(Long[]::new));
			} else if(TargetType.AUTH_OPERATION == notification.targetType()) {
				notificationApi.sendNotificationToAuthOperation(notification.concern(), notificationContext.getResult(), notification.target());
			}
		} catch(Exception e) {
			LOGGER.error("Send Notification ERROR", e);
		}
	}
	
	@Pointcut("@annotation(ekol.notification.annotation.SendNotification)")
	public void sendNotification() {}

	@Pointcut("@annotation(ekol.notification.annotation.SendNotifications)")
	public void sendNotifications() {}
	
}
