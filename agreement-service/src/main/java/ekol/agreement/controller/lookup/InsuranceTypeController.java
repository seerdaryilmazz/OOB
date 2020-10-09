package ekol.agreement.controller.lookup;

import ekol.agreement.domain.enumaration.InsuranceType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/insurance-type")
public class InsuranceTypeController extends BaseEnumApiController<InsuranceType> {

    @PostConstruct
    public void init(){
        setType(InsuranceType.class);
    }
}
