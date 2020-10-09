package ekol.usermgr.controller;


import ekol.resource.controller.BaseEnumApiController;
import ekol.usermgr.domain.UserStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/user-status")
public class UserStatusApiController extends BaseEnumApiController<UserStatus> {

    @PostConstruct
    public void init() {
        setType(UserStatus.class);
    }
}
