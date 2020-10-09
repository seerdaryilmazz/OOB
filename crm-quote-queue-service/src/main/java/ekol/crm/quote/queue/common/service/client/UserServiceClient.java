package ekol.crm.quote.queue.common.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import ekol.crm.quote.queue.common.dto.UserJson;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor=@__(@Autowired))
public class UserServiceClient {

    @Value("${oneorder.user-service}")
    private String userService;

    @NonNull
    private OAuth2RestTemplate restTemplate;

    /**
     * Kayıt bulunamadığında user-service'ten hata dönecektir çünkü null kontrolü yok.
     */
    public UserJson findUser(String username) {
        return restTemplate.getForObject(userService + "/users/{username}", UserJson.class, username);
    }
}
