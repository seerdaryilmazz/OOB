package ekol.location.controller;

import ekol.resource.controller.BaseEnumApiController;
import ekol.location.domain.RouteLegType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/route-leg-type")
public class RouteLegTypeController extends BaseEnumApiController<RouteLegType> {

    @PostConstruct
    public void init() {
        setType(RouteLegType.class);
    }
}
