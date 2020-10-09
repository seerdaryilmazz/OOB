package ekol.crm.quote.validator;

import ekol.crm.quote.domain.model.Price;
import ekol.exceptions.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class PriceValidator {

    public void validate(Price price){
        if(price.getQuote() == null || price.getQuote().getId() == null){
            throw new ValidationException("Price should be assigned to a quote");
        }
        if(price.getType() == null){
            throw new ValidationException("Price should have a type");
        }
        if(price.getBillingItem() == null){
            throw new ValidationException("Price should have a billing item");
        }
        if(price.getCharge() == null || price.getCharge().getAmount() == null){
            throw new ValidationException("Price should have an amount");
        }
        if(price.getCharge().getCurrency() == null){
            throw new ValidationException("Price should have a currency");
        }
    }
}
