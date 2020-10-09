package ekol.orders.transportOrder.controller;


import ekol.orders.transportOrder.domain.VehicleType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/vehicle-type")
public class VehicleTypeApiController extends BaseEnumApiController<VehicleType> {

    @PostConstruct
    public void init() {
        setType(VehicleType.class);
    }
}
