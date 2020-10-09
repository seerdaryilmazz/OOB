package ekol.crm.quote.controller.lookup;

import ekol.crm.quote.domain.enumaration.ExistenceType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/lookup/existence-type")
public class ExistenceTypeController extends BaseEnumApiController<ExistenceType> {

    @PostConstruct
    public void init() {
        setType(ExistenceType.class);
    }
}
