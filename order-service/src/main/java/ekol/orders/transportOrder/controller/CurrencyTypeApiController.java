package ekol.orders.transportOrder.controller;


import ekol.orders.transportOrder.domain.CurrencyType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/currency-type")
public class CurrencyTypeApiController extends BaseEnumApiController<CurrencyType> {

    @PostConstruct
    public void init() {
        setType(CurrencyType.class);
    }
}
