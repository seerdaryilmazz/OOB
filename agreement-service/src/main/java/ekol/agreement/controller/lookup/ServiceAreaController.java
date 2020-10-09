package ekol.agreement.controller.lookup;

import ekol.agreement.domain.enumaration.ServiceArea;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/service-area")
public class ServiceAreaController extends BaseEnumApiController<ServiceArea> {

    @PostConstruct
    public void init() {
        setType(ServiceArea.class);
    }
}
