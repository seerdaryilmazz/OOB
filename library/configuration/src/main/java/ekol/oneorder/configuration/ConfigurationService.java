package ekol.oneorder.configuration;

import java.util.Map;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ekol.oneorder.configuration.dto.Option;

@Component
public class ConfigurationService {
	
	@Value("${oneorder.configuration-management-service}")
	private String configurationManagementService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public Map<String, Option<?>> list(Long subsidiary){
		return Stream.of(restTemplate.getForObject(configurationManagementService + "/option?subsidiaryId={subsidiary}", Option[].class, subsidiary)).collect(Collectors.toMap(Option::getKey, t->t));
	}
	
	public <T> T evaluate(String key, Long subsidiary, Object facts, Class<T> returnType) {
		return restTemplate.postForObject(configurationManagementService + "/rule/evaluate?code={key}&subsidiaryId={subsidiary}", facts, returnType, key, subsidiary);
	}
}
