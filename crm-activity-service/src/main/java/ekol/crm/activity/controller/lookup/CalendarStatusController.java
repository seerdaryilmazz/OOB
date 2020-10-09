package ekol.crm.activity.controller.lookup;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.crm.activity.domain.CalendarStatus;
import ekol.resource.controller.BaseEnumApiController;


@RestController
@RequestMapping("/lookup/calendar-status")
public class CalendarStatusController extends BaseEnumApiController<CalendarStatus> {

    @PostConstruct
    public void init() {
        setType(CalendarStatus.class);
    }
}