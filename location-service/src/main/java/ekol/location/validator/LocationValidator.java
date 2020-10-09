package ekol.location.validator;

import ekol.exceptions.BadRequestException;
import ekol.location.domain.location.comnon.Location;
import org.springframework.stereotype.Component;

/**
 * Created by burak on 05/04/17.
 */
@Component
public class LocationValidator {

    public void validate(Location location) {
        if (location.getPointOnMap() == null) {
            throw new BadRequestException("Location should be marked on map");
        }
        if (location.getPointOnMap().getLat() == null) {
            throw new BadRequestException("Location point on map: Latitude can not be empty");
        }
        if (location.getPointOnMap().getLng() == null) {
            throw new BadRequestException("Location point on map: Longitude can not be empty");
        }
    }
}
