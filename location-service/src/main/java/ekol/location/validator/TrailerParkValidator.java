package ekol.location.validator;

import ekol.location.domain.location.trailerPark.TrailerPark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by kilimci on 02/05/2017.
 */
@Component
public class TrailerParkValidator {

    @Autowired
    private PlaceValidator placeValidator;

    public void validate(TrailerPark trailerPark) {
        placeValidator.validate(trailerPark);
    }
}
