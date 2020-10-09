package ekol.orders.transportOrder.controller;


import ekol.orders.order.domain.Status;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/status")
public class StatusApiController extends BaseEnumApiController<Status> {

    @PostConstruct
    public void init() {
        setType(Status.class);
    }
}
