package ekol.crm.activity.controller.lookup;

import ekol.crm.activity.domain.ActivityTool;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/lookup/activity-tool")
public class ActivityToolController extends BaseEnumApiController<ActivityTool> {

    @PostConstruct
    public void init() {
        setType(ActivityTool.class);
    }
}

