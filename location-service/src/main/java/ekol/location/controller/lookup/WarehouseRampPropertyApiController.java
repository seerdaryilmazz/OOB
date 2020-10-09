package ekol.location.controller.lookup;

import ekol.resource.controller.BaseEnumApiController;
import ekol.location.domain.location.enumeration.WarehouseRampProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by burak on 01/02/17.
 */
@RestController
@RequestMapping("/lookup/warehouse-ramp-property")
public class WarehouseRampPropertyApiController extends BaseEnumApiController<WarehouseRampProperty> {

    @PostConstruct
    public void init() {
        setType(WarehouseRampProperty.class);
    }
}