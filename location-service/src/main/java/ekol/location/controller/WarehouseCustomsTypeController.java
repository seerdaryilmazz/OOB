package ekol.location.controller;

import ekol.location.domain.WarehouseCustomsType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/lookup/warehouse-customs-type")
public class WarehouseCustomsTypeController extends BaseEnumApiController<WarehouseCustomsType> {

    @PostConstruct
    public void init() {
        setType(WarehouseCustomsType.class);
    }

    @RequestMapping(value = "/for-customs", method = RequestMethod.GET)
    public List<WarehouseCustomsType> listForCustoms() {
        return Arrays.asList(
                WarehouseCustomsType.NON_BONDED_WAREHOUSE,
                WarehouseCustomsType.CUSTOMS_WAREHOUSE,
                WarehouseCustomsType.CUSTOMS_CLEARANCE_PARK,
                WarehouseCustomsType.EUROPE_CUSTOMS_LOCATION);
    }

    @RequestMapping(value = "/for-companies", method = RequestMethod.GET)
    public List<WarehouseCustomsType> listForCompanies() {
        return Arrays.asList(WarehouseCustomsType.values());
    }

}
