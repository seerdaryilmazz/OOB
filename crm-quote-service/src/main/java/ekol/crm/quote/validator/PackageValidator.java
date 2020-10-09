package ekol.crm.quote.validator;

import ekol.crm.quote.domain.model.Package;
import ekol.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class PackageValidator {

    public void validate(Package quotePackage){
        if(quotePackage.getQuote() == null || quotePackage.getQuote().getId() == null){
            throw new ValidationException("Quote package should be assigned to a quote");
        }
        if(StringUtils.isBlank(quotePackage.getType())){
            throw new ValidationException("Quote package should have a type");
        }
        if(quotePackage.getQuantity() == null){
            throw new ValidationException("Quote package should have a quantity");
        }
    }
}
