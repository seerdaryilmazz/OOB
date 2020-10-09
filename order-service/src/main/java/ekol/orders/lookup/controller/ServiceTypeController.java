package ekol.orders.lookup.controller;


import ekol.orders.lookup.domain.ServiceType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/service-type")
public class ServiceTypeController extends BaseEnumApiController<ServiceType> {

    @PostConstruct
    public void init() {
        setType(ServiceType.class);
    }
}
