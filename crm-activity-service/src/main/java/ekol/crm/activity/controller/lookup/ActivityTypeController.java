package ekol.crm.activity.controller.lookup;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ekol.crm.activity.domain.ActivityTool;
import ekol.crm.activity.domain.ActivityType;
import ekol.resource.controller.BaseEnumApiController;


@RestController
@RequestMapping("/lookup/activity-type")
public class ActivityTypeController extends BaseEnumApiController<ActivityType> {

    @PostConstruct
    public void init() {
        setType(ActivityType.class);
    }
    
    @GetMapping("/by-tool")
    public List<ActivityType> listByActivityTool(@RequestParam(required=false) ActivityTool activityTool){
    	List<ActivityType> types = new LinkedList<>(Arrays.asList(ActivityType.values()));
    	if(ActivityTool.MEETING != activityTool) {
    		types.remove(ActivityType.FAIR);
    	}
    	return types;
    }
}

