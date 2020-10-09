package ekol.location.service;

import ekol.location.domain.location.comnon.Location;
import ekol.location.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by kilimci on 09/05/2017.
 */
@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public Location save(Location location){
        return locationRepository.save(location);
    }

}
