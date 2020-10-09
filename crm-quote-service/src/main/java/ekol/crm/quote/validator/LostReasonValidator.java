package ekol.crm.quote.validator;

import ekol.crm.quote.domain.model.LostReason;
import ekol.exceptions.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class LostReasonValidator {

    public void validate(LostReason lostReason){
        if(lostReason.getReason() == null){
            throw new ValidationException("Lost quote should have a reason");
        }
    }
}
