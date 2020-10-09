package ekol.crm.quote.validator;

import ekol.crm.quote.domain.model.PriceDiscount;
import ekol.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class PriceDiscountValidator {

    public void validate(PriceDiscount priceDiscount){
        if(priceDiscount.getPrice() == null || priceDiscount.getPrice().getId() == null){
            throw new ValidationException("Discount should be assigned to a price");
        }
        if(priceDiscount.getSalesPriceId() == null){
            throw new ValidationException("Discount should have a sales price id");
        }
        if(StringUtils.isBlank(priceDiscount.getName())){
            throw new ValidationException("Discount should have a name");
        }
        if(priceDiscount.getAmount() == null){
            throw new ValidationException("Discount amount should not be empty");
        }
        if(priceDiscount.getPercentage() == null){
            throw new ValidationException("Discount percentage should not be empty");
        }
    }
}
