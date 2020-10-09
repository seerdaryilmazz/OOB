package ekol.agreement.controller.lookup;

import ekol.agreement.domain.enumaration.BasedOnType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/basedOn-type")
public class BasedOnTypeController extends BaseEnumApiController<BasedOnType> {

    @PostConstruct
    public void init() {
        setType(BasedOnType.class);
    }
}