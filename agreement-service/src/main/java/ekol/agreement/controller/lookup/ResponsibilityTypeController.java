package ekol.agreement.controller.lookup;

import ekol.agreement.domain.enumaration.ResponsibilityType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/responsibility-type")
public class ResponsibilityTypeController extends BaseEnumApiController<ResponsibilityType> {

    @PostConstruct
    public void init(){
        setType(ResponsibilityType.class);
    }
}
