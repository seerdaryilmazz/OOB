package ekol.crm.activity.controller.lookup;

import ekol.crm.activity.domain.ShowAs;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/lookup/show-as")
public class ShowAsController extends BaseEnumApiController<ShowAs> {

    @PostConstruct
    public void init() {
        setType(ShowAs.class);
    }
}

