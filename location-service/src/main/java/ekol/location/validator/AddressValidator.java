package ekol.location.validator;

import ekol.exceptions.BadRequestException;
import ekol.location.domain.location.comnon.Address;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by kilimci on 28/04/2017.
 */
@Component
public class AddressValidator {

    public void validate(Address address){
        if (StringUtils.isEmpty(address.getStreetAddress())) {
            throw new BadRequestException("Address can not be empty");
        }

        if (address.getCountry() == null || StringUtils.isEmpty(address.getCountry().getIso())) {
            throw new BadRequestException("Country can not be empty");
        }
    }
}
