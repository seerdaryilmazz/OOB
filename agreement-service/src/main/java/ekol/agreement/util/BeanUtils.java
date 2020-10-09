package ekol.agreement.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BeanUtils {
	private static BeanUtils instance;
	
	@Autowired
	private ApplicationContext context;
	
	@PostConstruct
	private void register() {
		instance = this;
	}
	
	public static <T> T getBean(Class<T> beanClass) {
		return instance.context.getBean(beanClass);
	}
	
}
