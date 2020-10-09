package ekol.orders.search.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ekol.orders.lookup.domain.HSCodeExtended;
import ekol.orders.search.service.HSCodeExtendedIndexService;

@Aspect
@Component
public class HSCodeExtendedIndexAspect {
	
	@Autowired
	private HSCodeExtendedIndexService hscodeExtendedIndexService;

	@Pointcut("execution(* ekol.orders.lookup.repository.HSCodeExtendedRepository.save(..))")
	public void saveHSCodeExtended() {
	};

	@AfterReturning(value= "saveHSCodeExtended()", returning="entity")
	public void afterSave(JoinPoint joinPoint, HSCodeExtended entity) throws Throwable {
		if(entity.isDeleted()) {
			hscodeExtendedIndexService.removeHSCode(entity);
		} else {
			hscodeExtendedIndexService.indexHSCode(entity);
		}
	}

	@AfterReturning(value= "saveHSCodeExtended()", returning="entity")
	public void afterSave(JoinPoint joinPoint, Iterable<HSCodeExtended> entity) throws Throwable {
		entity.forEach(hscodeExtendedIndexService::indexHSCode);
	}
}
