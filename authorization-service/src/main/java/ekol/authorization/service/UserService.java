package ekol.authorization.service;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ekol.model.User;

@Service
public class UserService {

    @Value("${oneorder.user-service}")
    private String userServiceName;

    @Autowired
    private RestTemplate restTemplata;
    
    public <T> List<T> getUsersByUsername(Set<String> usernames, String status,String authenticationType, Class<T[]> componentType) {
    	return Arrays.asList(restTemplata.postForObject(userServiceName + "/users/by-username?status={status}&authenticationType={authenticationType}", usernames, componentType, status, authenticationType));
    }

    public <T> T getUserByAccountName(String username, Class<T> type) {
        return restTemplata.getForObject(userServiceName + "/users/" + username, type);
    }
}
