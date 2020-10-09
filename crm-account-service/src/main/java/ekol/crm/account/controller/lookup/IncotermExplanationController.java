package ekol.crm.account.controller.lookup;

import ekol.crm.account.domain.enumaration.IncotermExplanation;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/lookup/incoterm-explanation")
public class IncotermExplanationController extends BaseEnumApiController<IncotermExplanation> {

    @PostConstruct
    public void init() {
        setType(IncotermExplanation.class);
    }
}
