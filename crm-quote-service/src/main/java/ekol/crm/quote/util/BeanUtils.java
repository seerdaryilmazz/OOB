package ekol.crm.quote.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanUtils {
	private static BeanUtils instance;
	
	private static void setInstance(BeanUtils instance) {
		BeanUtils.instance = instance;
	}

	@Autowired
	private ApplicationContext context;
	
	@PostConstruct
	private synchronized void register() {
		BeanUtils.setInstance(this);
	}
	
	public static <T> T getBean(Class<T> beanClass) {
		return instance.context.getBean(beanClass);
	}
}
