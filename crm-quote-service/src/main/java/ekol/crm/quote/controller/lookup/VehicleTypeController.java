package ekol.crm.quote.controller.lookup;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.crm.quote.domain.enumaration.VehicleType;
import ekol.resource.controller.BaseEnumApiController;

@RestController
@RequestMapping("/lookup/vehicle-type")
public class VehicleTypeController extends BaseEnumApiController<VehicleType>{
	
	@PostConstruct
    public void init() {
        setType(VehicleType.class);
    }
	

}
