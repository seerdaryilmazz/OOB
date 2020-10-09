package ekol.crm.account.validator;

import ekol.crm.account.domain.model.CustomsOffice;
import ekol.crm.account.repository.CustomsOfficeRepository;
import ekol.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CustomsOfficeValidator {

    private CustomsOfficeRepository customsOfficeRepository;

    public void validate(CustomsOffice customsOffice){

        if(customsOffice.getOffice() == null || customsOffice.getOffice().getId() == null){
            throw new ValidationException("Customs Office should not be empty");
        }
    }


}
