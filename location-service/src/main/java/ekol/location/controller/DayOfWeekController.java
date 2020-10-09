package ekol.location.controller;

import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.time.DayOfWeek;

@RestController
@RequestMapping("/lookup/day-of-week")
public class DayOfWeekController extends BaseEnumApiController<DayOfWeek> {

    @PostConstruct
    public void init() {
        setType(DayOfWeek.class);
    }
}
