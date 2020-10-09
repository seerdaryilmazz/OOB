package ekol.crm.quote.client;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ekol.model.User;

@Service
public class UserService {

	@Value("${oneorder.user-service}") 
    private String userServiceName;

	@Autowired
    private RestTemplate restTemplate;

    /**
     * Kayıt bulunamadığında user-service'ten hata dönecektir çünkü null kontrolü yok.
     */
    public User findUser(String username) {
        return restTemplate.getForObject(userServiceName + "/users/{username}", User.class, username);
    }
}
