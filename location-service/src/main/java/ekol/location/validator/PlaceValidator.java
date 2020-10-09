package ekol.location.validator;

import ekol.exceptions.BadRequestException;
import ekol.location.domain.location.comnon.Place;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by kilimci on 28/04/2017.
 */
@Component
public class PlaceValidator {

    @Autowired
    private EstablishmentValidator establishmentValidator;

    @Autowired
    private LocationValidator locationValidator;

    public void validate(Place place){
        if (StringUtils.isEmpty(place.getName())) {
            throw new BadRequestException("Name can not be empty");
        }

        if (StringUtils.isEmpty(place.getLocalName())) {
            throw new BadRequestException("Local Name can not be empty");
        }

        if(place.getLocation() == null){
            throw new BadRequestException("Location can not be empty");
        } else {
            locationValidator.validate(place.getLocation());
        }

        if(place.getEstablishment() != null){
            establishmentValidator.validate(place.getEstablishment());
        }

    }
}
