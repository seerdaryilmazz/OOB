package ekol.crm.account.controller.lookup;

import ekol.crm.account.domain.enumaration.CustomsType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/lookup/customs-type")
public class CustomsTypeController extends BaseEnumApiController<CustomsType> {

    @PostConstruct
    public void init() {
        setType(CustomsType.class);
    }
}
