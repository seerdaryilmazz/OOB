package ekol.orders.lookup.controller;


import ekol.orders.lookup.domain.TruckLoadType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/truck-load-type")
public class TruckLoadTypeController extends BaseEnumApiController<TruckLoadType> {

    @PostConstruct
    public void init() {
        setType(TruckLoadType.class);
    }
}
