package ekol.orders.lookup.aspect;

import java.util.Objects;
import java.util.Optional;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ekol.exceptions.BadRequestException;
import ekol.orders.lookup.domain.HSCodeExtended;
import ekol.orders.lookup.repository.HSCodeExtendedRepository;

@Aspect
@Component
public class HSCodeExtendedAspect {

	@Autowired
	private HSCodeExtendedRepository  hsCodeExtendedRepository;

	@Pointcut("execution(* ekol.orders.lookup.repository.HSCodeExtendedRepository.save(..))")
	public void saveHSCodeExtended() { }

	@Before(value = "saveHSCodeExtended() && args(var1)")
	public void beforeSave(JoinPoint joinPoint, HSCodeExtended var1) throws Throwable {
		checkName(var1);
	}

	@Before(value = "saveHSCodeExtended() && args(var1)")
	public void beforeSave(JoinPoint joinPoint, Iterable<HSCodeExtended> var1) throws Throwable {
		for (HSCodeExtended hsCodeExtended : var1) {
			checkName(hsCodeExtended);
		}
	}

	private void checkName(HSCodeExtended var1) {
		if(!var1.isDeleted() && Optional.ofNullable(hsCodeExtendedRepository.findByName(var1.getName())).filter(t->!Objects.equals(t.getId(), var1.getId())).isPresent()){
			throw new BadRequestException("Definition ({0}) must be unique",var1.getName());
		}
	}
}
