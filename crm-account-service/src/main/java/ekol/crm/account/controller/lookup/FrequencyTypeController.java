package ekol.crm.account.controller.lookup;

import ekol.crm.account.domain.enumaration.FrequencyType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/lookup/frequency-type")
public class FrequencyTypeController extends BaseEnumApiController<FrequencyType> {

    @PostConstruct
    public void init() {
        setType(FrequencyType.class);
    }
}
