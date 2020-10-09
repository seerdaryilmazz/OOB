package ekol.crm.quote.validator;

import ekol.crm.quote.domain.model.Load;
import ekol.exceptions.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class LoadValidator {

    public void validate(Load load){
        if(load.getQuote() == null || load.getQuote().getId() == null){
            throw new ValidationException("Quote load should be assigned to a quote");
        }
        if(load.getType() == null){
            throw new ValidationException("Quote load should have a type");
        }
    }
}
