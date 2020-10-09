package ekol.config;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisCacheConfiguration extends CachingConfigurerSupport {

	@Value("${spring.application.name}")
	private String applicationName;
	
	@Override
    public KeyGenerator keyGenerator(){
        return new RedisCacheKeyGenerator(applicationName);
    }
	
	@Bean
	public CacheManager cacheManager(Map<String, Integer> ttl, RedisTemplate<?,?> redisTemplate) {
		RedisCacheManager manager = new RedisCacheManager(redisTemplate);
		manager.setExpires(cast(ttl));
		return manager;
	}

	@Bean
	@ConfigurationProperties("spring.cache.ttl")
	public Map<String, Integer> ttl() {
		return new HashMap<>();
	}

	private Map<String, Long> cast(Map<String, Integer> source) {
		Map<String, Long> target = new HashMap<>();
		source.entrySet().forEach(entry-> target.put(entry.getKey(), Long.valueOf(String.valueOf(entry.getValue()))));
		return target;
	}

}
