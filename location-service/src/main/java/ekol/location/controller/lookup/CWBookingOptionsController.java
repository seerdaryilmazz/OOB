package ekol.location.controller.lookup;

import ekol.location.domain.location.enumeration.CWBookingOptions;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/cw-booking-options")
public class CWBookingOptionsController extends BaseEnumApiController<CWBookingOptions> {

    @PostConstruct
    public void init() {
        setType(CWBookingOptions.class);
    }
}
