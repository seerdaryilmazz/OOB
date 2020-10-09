package ekol.resource.validation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ComponentScan
public class ValidationAspect implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	@Around("ekol.resource.validation.ValidationAspect.oneOrderValidation() || ekol.resource.validation.ValidationAspect.oneOrderValidations()")
	public Object aroundOneOrderValidation(ProceedingJoinPoint call) throws Throwable {
		OneOrderValidation[] oneOrderValidations = ((MethodSignature) call.getSignature()).getMethod().getAnnotationsByType(OneOrderValidation.class);
		for (OneOrderValidation oneOrderValidation : oneOrderValidations) {
			applicationContext.getBean(oneOrderValidation.value()).validate(call.getArgs());
		}
		return call.proceed();
	}
	
	@Pointcut("@annotation(ekol.resource.validation.OneOrderValidation)")
	public void oneOrderValidation() {}

	@Pointcut("@annotation(ekol.resource.validation.OneOrderValidations)")
	public void oneOrderValidations() {}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext; 
	}
}
