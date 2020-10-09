package ekol.kartoteks.service;

import ekol.resource.oauth2.SessionOwner;
import ekol.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.verify;

/**
 * Created by kilimci on 22/11/16.
 */
@RunWith(SpringRunner.class)
public class UserServiceUnitTest {

    @Mock
    private SessionOwner sessionOwner;

    @Mock
    private OAuth2RestTemplate resourceRestTemplate;

    @InjectMocks
    private UserService userService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(userService, "userServiceName", "user-service");
    }

    @Test
    public void shouldGetSessionUser() {
        userService.getSessionUser();
        verify(sessionOwner).getCurrentUser();
    }

    @Test
    public void getUserByAccountName() {
        String username = "username";
        userService.getUserByAccountName(username);
        verify(resourceRestTemplate).getForObject("user-service/users/username", User.class);

    }

}
