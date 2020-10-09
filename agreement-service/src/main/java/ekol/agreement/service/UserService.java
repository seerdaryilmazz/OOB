package ekol.agreement.service;

import ekol.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

	@Value("${oneorder.user-service}") 
    private String userServiceName;

	@Autowired
    private RestTemplate restTemplate;

    /**
     * Kayıt bulunamadığında user-service'ten hata dönecektir çünkü null kontrolü yok.
     */
    public User[] findUsers() {
        return restTemplate.getForObject(userServiceName + "/users", User[].class);
    }
}
