package ekol.crm.quote.validator;

import ekol.crm.quote.domain.model.Document;
import ekol.exceptions.ValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DocumentValidator {

    public void validate(Document document){
        if(document.getQuote() == null || document.getQuote().getId() == null){
            throw new ValidationException("Document should be assigned to a quote");
        }
        if(StringUtils.isEmpty(document.getDocumentId())){
            throw new ValidationException("Document should have a id");
        }
        if(document.getDocumentName().length()>=100)
        {
        	throw new ValidationException("File names can be 100 characters long as maximum!");
        }
        
    }
}
