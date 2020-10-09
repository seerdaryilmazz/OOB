package ekol.location.controller.lookup;

import ekol.resource.controller.BaseEnumApiController;
import ekol.location.domain.location.enumeration.WarehouseRampUsageType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by burak on 01/02/17.
 */
@RestController
@RequestMapping("/lookup/warehouse-ramp-usage-type")
public class WarehouseRampUsageTypeApiController extends BaseEnumApiController<WarehouseRampUsageType> {

    @PostConstruct
    public void init() {
        setType(WarehouseRampUsageType.class);
    }
}