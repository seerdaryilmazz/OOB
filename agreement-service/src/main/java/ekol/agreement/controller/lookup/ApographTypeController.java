package ekol.agreement.controller.lookup;

import ekol.agreement.domain.enumaration.ApographType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/apograph-type")
public class ApographTypeController extends BaseEnumApiController<ApographType> {

    @PostConstruct
    public void init() {
        setType(ApographType.class);
    }
}
