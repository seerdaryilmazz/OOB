package ekol.location.validator;

import ekol.location.domain.location.comnon.Establishment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by kilimci on 28/04/2017.
 */
@Component
public class EstablishmentValidator {

    @Autowired
    private AddressValidator addressValidator;

    public void validate(Establishment establishment){
        if(establishment.getAddress() != null){
            addressValidator.validate(establishment.getAddress());
        }


    }

}
