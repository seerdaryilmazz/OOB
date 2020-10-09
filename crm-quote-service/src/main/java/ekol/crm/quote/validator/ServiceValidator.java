package ekol.crm.quote.validator;

import ekol.crm.quote.domain.model.Service;
import ekol.exceptions.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class ServiceValidator {

    public void validate(Service service){
        if(service.getQuote() == null || service.getQuote().getId() == null){
            throw new ValidationException("Service should be assigned to a quote");
        }
        if(service.getType() == null){
            throw new ValidationException("Service should have a type");
        }
    }
}
