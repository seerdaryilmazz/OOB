package ekol.crm.account.validator;

import ekol.crm.account.domain.model.Contact;
import ekol.crm.account.repository.AccountRepository;
import ekol.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ContactValidator {

    private AccountRepository accountRepository;

    public void validate(Contact contact){
        if(contact.getAccount() == null || contact.getAccount().getId() == null){
            throw new ValidationException("Account should be assigned to a contact");
        }
        if(!accountRepository.findById(contact.getAccount().getId()).isPresent()){
            throw new ValidationException("Account with id can not found");
        }
        if(contact.getCompanyContactId() == null){
            throw new ValidationException("Company contact should not be empty");
        }
        if(StringUtils.isBlank(contact.getFirstName()) ||
                StringUtils.isBlank(contact.getLastName())){
            throw new ValidationException("Contact name should not be empty");
        }
    }
}
