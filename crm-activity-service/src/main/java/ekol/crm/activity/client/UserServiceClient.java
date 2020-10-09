package ekol.crm.activity.client;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import ekol.model.User;

@Component
public class UserServiceClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceClient.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${oneorder.user-service}")
	private String userService;

	public User getUserDetail(String username) {
		try {
			return restTemplate.getForObject(userService + "/users/{username}", User.class, username);
		} catch(HttpClientErrorException e){
			LOGGER.error("User not found. username: {}", username);
			throw e;
		}
	}
	
	public User[] searchUsers(String authenticationType, Boolean inactiveUsers) {
		return restTemplate.getForObject(userService + "/users/search?authenticationType="+ authenticationType + "&inactiveUsers=" + inactiveUsers, User[].class);
	}
}
