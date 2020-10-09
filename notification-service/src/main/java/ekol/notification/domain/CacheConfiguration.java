package ekol.notification.domain;

import java.util.*;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix="spring.cache")
public class CacheConfiguration {
	private List<String> cacheNames = new ArrayList<>();
}
