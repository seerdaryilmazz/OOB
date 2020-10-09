package ekol.location.controller.lookup;

import ekol.resource.controller.BaseEnumApiController;
import ekol.location.domain.location.enumeration.WarehouseOwnerType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by kilimci on 02/05/2017.
 */
@RestController
@RequestMapping("/lookup/warehouse-owner-type")
public class WarehouseOwnerTypeController extends BaseEnumApiController<WarehouseOwnerType> {

    @PostConstruct
    public void init() {
        setType(WarehouseOwnerType.class);

    }
}
