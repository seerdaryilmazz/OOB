package ekol.agreement.controller.lookup;

import ekol.agreement.domain.enumaration.RenewalDateType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/renewal-date-type")
public class RenewalDateTypeController extends BaseEnumApiController<RenewalDateType> {

    @PostConstruct
    public void init() {
        setType(RenewalDateType.class);
    }
}
