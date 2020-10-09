package ekol.crm.activity.controller.lookup;

import ekol.crm.activity.domain.ActivityScope;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;


@RestController
@RequestMapping("/lookup/activity-scope")
public class ActivityScopeController extends BaseEnumApiController<ActivityScope> {

    @PostConstruct
    public void init() {
        setType(ActivityScope.class);
    }
}