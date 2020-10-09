package ekol.crm.inbound.client;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ekol.crm.inbound.domain.User;

@Component
public class UserServiceClient {
	
	@Value("${oneorder.user-service}")
	private String userServiceUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public User searchUser(String email) {
		return Stream.of(restTemplate.getForObject(userServiceUrl +"/users/search?email={email}", User[].class, email)).findFirst().orElse(null);
	}

}
