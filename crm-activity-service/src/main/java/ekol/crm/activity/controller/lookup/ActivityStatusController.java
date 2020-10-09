package ekol.crm.activity.controller.lookup;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.crm.activity.domain.ActivityStatus;
import ekol.resource.controller.BaseEnumApiController;


@RestController
@RequestMapping("/lookup/activity-status")
public class ActivityStatusController extends BaseEnumApiController<ActivityStatus> {

    @PostConstruct
    public void init() {
        setType(ActivityStatus.class);
    }
}