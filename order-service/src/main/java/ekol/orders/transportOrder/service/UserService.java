package ekol.orders.transportOrder.service;

import ekol.model.User;
import ekol.resource.oauth2.SessionOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by kilimci on 30/09/16.
 */
@Service
public class UserService {

    @Autowired
    private SessionOwner sessionOwner;

    public String getSessionUsername() {
        return sessionOwner.getCurrentUser().getUsername();
    }

    public User getCurrentUser() {
        return sessionOwner.getCurrentUser();
    }
}
