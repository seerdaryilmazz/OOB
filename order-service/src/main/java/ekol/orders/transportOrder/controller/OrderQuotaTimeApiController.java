package ekol.orders.transportOrder.controller;


import ekol.orders.transportOrder.domain.OrderQuotaTime;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by burak on 18/10/16.
 */
@RestController
@RequestMapping("/lookup/order-quota-time")
public class OrderQuotaTimeApiController extends BaseEnumApiController<OrderQuotaTime> {

    @PostConstruct
    public void init() {
        setType(OrderQuotaTime.class);
    }
}