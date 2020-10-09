package ekol.location.controller;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.location.domain.ZoneType;
import ekol.location.repository.ZoneTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by ozer on 13/12/16.
 */
@RestController
@RequestMapping("/lookup/zone-type")
public class ZoneTypeController extends BaseLookupApiController<ZoneType> {

    @Autowired
    private ZoneTypeRepository zoneTypeRepository;

    @PostConstruct
    public void init() {
        setLookupRepository(zoneTypeRepository);
    }
}
