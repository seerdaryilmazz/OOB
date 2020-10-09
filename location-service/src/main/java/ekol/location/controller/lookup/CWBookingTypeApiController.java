package ekol.location.controller.lookup;

import ekol.resource.controller.BaseEnumApiController;
import ekol.location.domain.location.enumeration.CWBookingType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by burak on 11/04/17.
 */
@RestController
@RequestMapping("/lookup/cw-booking-type")
public class CWBookingTypeApiController extends BaseEnumApiController<CWBookingType> {

    @PostConstruct
    public void init() {
        setType(CWBookingType.class);
    }
}
