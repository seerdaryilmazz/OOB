package ekol.notification.client;

import org.springframework.beans.factory.annotation.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import ekol.notification.client.dto.User;
import lombok.Getter;

@Getter
@Component
public class UserServiceClient {

	@Value("${oneorder.user-service}")
	private String userServiceUrl;

	@Value("${oneorder.gateway}")
	private String gatewayUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Cacheable("one-week-cache")
	public User getUser(Long id) {
		try {
			return restTemplate.getForObject(userServiceUrl + "/users?id={id}", User.class, id);
		} catch(HttpClientErrorException hcee) {
			if(!hcee.getStatusCode().is4xxClientError()) {
				throw hcee;
			}
		}
		return null;
	}
	
	@Cacheable("one-week-cache")
	public User getUser(String username) {
		try {
			return restTemplate.getForObject(userServiceUrl + "/users/{username}", User.class, username);
		} catch(HttpClientErrorException hcee) {
			if(!hcee.getStatusCode().is4xxClientError()) {
				throw hcee;
			}
		}
		return null;
	}
	
}
