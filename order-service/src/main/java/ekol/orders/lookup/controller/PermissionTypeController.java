package ekol.orders.lookup.controller;

import ekol.orders.lookup.domain.PermissionType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/permission-type")
public class PermissionTypeController extends BaseEnumApiController<PermissionType> {

    @PostConstruct
    public void init() {
        setType(PermissionType.class);
    }
}
