package ekol.usermgr.controller;

import ekol.resource.controller.BaseEnumApiController;
import ekol.usermgr.domain.UserAuthenticationType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by ozer on 17/02/2017.
 */
@RestController
@RequestMapping("/lookup/user-authentication-type")
public class UserAuthenticationTypeApiController extends BaseEnumApiController<UserAuthenticationType> {

    @PostConstruct
    public void init() {
        setType(UserAuthenticationType.class);
    }
}
