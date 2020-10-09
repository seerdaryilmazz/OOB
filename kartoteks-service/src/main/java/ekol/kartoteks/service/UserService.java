package ekol.kartoteks.service;

import ekol.kartoteks.domain.dto.UserJson;
import ekol.resource.oauth2.SessionOwner;
import ekol.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by kilimci on 20/09/16.
 */
@Service
public class UserService {

    @Value("${oneorder.user-service}")
    private String userServiceName;

    @Autowired
    private SessionOwner sessionOwner;

    @Autowired
    private OAuth2RestTemplate resourceRestTemplate;

    public User getSessionUser() {
        return sessionOwner.getCurrentUser();
    }

    public User getUserByAccountName(String username) {
        return resourceRestTemplate.getForObject(userServiceName + "/users/" + username, User.class);
    }

    public UserJson findUser(String username) {
        String url = userServiceName + "/users/" + username;
        return resourceRestTemplate.getForObject(url, UserJson.class);
    }
}
