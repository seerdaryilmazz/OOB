package ekol.agreement.client;

import java.util.*;
import java.util.stream.*;

import org.apache.commons.collections4.SetUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ekol.model.*;

@Component
public class AuthorizationServiceClient {
	
	@Value("${oneorder.authorization-service}")
	private String url;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public boolean isAccess(String... operations) {
		Set<String> myOperations = Stream.of(restTemplate.getForObject(url +"/auth/operation/my", IdNamePair[].class)).map(IdNamePair::getName).collect(Collectors.toSet());
		Set<String> ops = Stream.of(operations).collect(Collectors.toSet());
		return !SetUtils.intersection(myOperations, ops).isEmpty();
	}
	
	public List<User> listUserManagers(String username) {
		Map<String, String> body = new LinkedHashMap<>();
		body.put("username", username);
		return Arrays.asList(restTemplate.postForObject(url + "/auth/user/user-manager", body, User[].class));
	}
}
