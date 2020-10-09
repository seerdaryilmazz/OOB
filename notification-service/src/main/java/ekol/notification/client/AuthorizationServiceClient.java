package ekol.notification.client;

import java.util.*;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ekol.notification.client.dto.*;
import lombok.Getter;

@Getter
@Component
public class AuthorizationServiceClient {
	@Value("${oneorder.authorization-service}")
	private String authorizationServiceUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	UserServiceClient userServiceClient;
	
	public Set<User> getUser(String operationName) {
		AuthUser[] authUsers = restTemplate.getForObject(authorizationServiceUrl + "/auth/user/byOperation?name={operationName}", AuthUser[].class, operationName);
		return Stream.of(authUsers).map(AuthUser::getName).map(userServiceClient::getUser).filter(Objects::nonNull).collect(Collectors.toSet());
	}
}
