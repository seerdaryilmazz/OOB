package ekol.location.controller;

import ekol.resource.controller.BaseEnumApiController;
import ekol.location.domain.LocationType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/location-type")
public class LocationTypeController extends BaseEnumApiController<LocationType> {

    @PostConstruct
    public void init() {
        setType(LocationType.class);
    }
}
