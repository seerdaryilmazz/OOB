package ekol.orders.lookup.controller;

import ekol.orders.lookup.domain.SpecialLoadType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/special-load-type")
public class SpecialLoadTypeController extends BaseEnumApiController<SpecialLoadType> {

    @PostConstruct
    public void init() {
        setType(SpecialLoadType.class);
    }
}