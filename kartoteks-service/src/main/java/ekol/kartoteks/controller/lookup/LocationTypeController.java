package ekol.kartoteks.controller.lookup;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.kartoteks.domain.LocationType;
import ekol.kartoteks.repository.LocationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by fatmaozyildirim on 5/3/16.
 */
@RestController
@RequestMapping("/location-type")
public class LocationTypeController extends BaseLookupApiController<LocationType> {

    @Autowired
    private LocationTypeRepository locationTypeRepository;

    @PostConstruct
    public void init(){
        setLookupRepository(locationTypeRepository);
    }
}
