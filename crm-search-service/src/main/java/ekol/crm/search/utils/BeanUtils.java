package ekol.crm.search.utils;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

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
