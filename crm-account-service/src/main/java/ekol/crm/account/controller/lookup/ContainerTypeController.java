package ekol.crm.account.controller.lookup;

import ekol.crm.account.domain.enumaration.ContainerType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/lookup/container-type")
public class ContainerTypeController extends BaseEnumApiController<ContainerType> {

    @PostConstruct
    public void init() {
        setType(ContainerType.class);
    }
}
