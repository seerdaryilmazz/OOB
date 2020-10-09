package ekol.crm.account.controller.lookup;

import ekol.crm.account.domain.enumaration.LoadWeightType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/lookup/load-weight-type")
public class LoadWeightTypeController extends BaseEnumApiController<LoadWeightType> {

    @PostConstruct
    public void init() {
        setType(LoadWeightType.class);
    }
}
