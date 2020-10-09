package ekol.crm.account.controller.lookup;

import ekol.crm.account.domain.enumaration.SegmentType;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/lookup/segment-type")
public class SegmentTypeController extends BaseEnumApiController<SegmentType> {

    @PostConstruct
    public void init() {
        setType(SegmentType.class);
    }
}
