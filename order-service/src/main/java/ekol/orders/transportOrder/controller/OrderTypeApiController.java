package ekol.orders.transportOrder.controller;


import ekol.orders.transportOrder.domain.OrderType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/order-type")
public class OrderTypeApiController extends BaseEnumApiController<OrderType> {

    @PostConstruct
    public void init() {
        setType(OrderType.class);
    }
}
