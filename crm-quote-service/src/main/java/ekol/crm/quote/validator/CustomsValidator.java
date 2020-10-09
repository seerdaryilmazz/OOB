package ekol.crm.quote.validator;

import ekol.crm.quote.domain.model.Customs;
import ekol.exceptions.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class CustomsValidator {

    public void validate(Customs customs) {
        if (customs.getDeparture() == null) {
            throw new ValidationException("Departure customs info should not be empty");
        }
        if (customs.getArrival() == null) {
            throw new ValidationException("Arrival customs info should not be empty");
        }
    }
}
