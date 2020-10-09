package ekol.crm.quote.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ekol.crm.quote.domain.enumaration.QuoteType;
import ekol.crm.quote.domain.model.product.Product;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.exceptions.ValidationException;

@Component
public class ProductValidator {

    public void validate(Product product){

        if(product.getServiceArea() == null){
            throw new ValidationException("Product should have service area info");
        }

        if(!(product.getServiceArea().equals("CCL") || product.getServiceArea().equals("WHM")) ){
            if(product.getFromCountry() == null || StringUtils.isEmpty(product.getFromCountry().getIso())){
                throw new ValidationException("From country should not be empty");
            }
            if(product.getToCountry() == null || StringUtils.isEmpty(product.getToCountry().getIso())){
                throw new ValidationException("To country should not be empty");
            }
            Quote quote = product.getQuote();
            if (quote.getType() != QuoteType.TENDER) {
                if (StringUtils.isEmpty(product.getFromPoint())) {
                    throw new ValidationException("From country point should not be empty");
                }
            }
        }
        if(product.getStatus() == null){
            throw new ValidationException("Product should have a status");
        }
    }
}
