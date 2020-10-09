package ekol.config;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

public class RedisCacheKeyGenerator implements KeyGenerator {
	
	private final String applicationName;
	
	public RedisCacheKeyGenerator(String applicationName) {
		this.applicationName = applicationName;
	}
	
	@Override
	public Object generate(Object target, Method method, Object... params) {
		return new RedisCacheKey(applicationName, target.getClass().getName(), method.getName(), params);
	}

}
