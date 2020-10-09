package ekol.location.validator;

import ekol.exceptions.BadRequestException;
import ekol.location.domain.location.port.Port;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by burak on 11/04/17.
 */
@Component
public class PortValidator {

    @Autowired
    private PlaceValidator placeValidator;

    public void validate(Port port) {

        placeValidator.validate(port);

        if (port.getWorkingHours() == null || port.getWorkingHours().size() == 0) {
            throw new BadRequestException("Working hours can not be empty");
        }

        if(port.getAssets() != null) {
            port.getAssets().forEach( asset -> {
                if(asset.getType() == null) {
                    throw new BadRequestException("Asset Type can not be Empty.");
                }
                int count = 0;
                port.getAssets().forEach( asset1 -> {
                    if(asset.getType() == asset1.getType() && asset != asset1) {
                        throw new BadRequestException("Same Asset Type is defined more than once.");
                    }
                });
            });
        }

    }
}
