package ekol.orders.lookup.controller;

import ekol.orders.lookup.domain.CustomsOperationType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/customs-operation-type")
public class CustomsOperationTypeController extends BaseEnumApiController<CustomsOperationType> {

    @PostConstruct
    public void init() {
        setType(CustomsOperationType.class);
    }
}
