package ekol.agreement.controller.lookup;

import ekol.agreement.domain.enumaration.AgreementType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/agreement-type")
public class AgreementTypeController extends BaseEnumApiController<AgreementType> {

    @PostConstruct
    public void init() {
        setType(AgreementType.class);
    }
}
