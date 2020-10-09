package ekol.kartoteks.domain.validator;

import ekol.exceptions.BadRequestException;
import ekol.kartoteks.domain.CompanyContact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by kilimci on 07/06/16.
 */
@Component
public class CompanyContactValidator {

    @Autowired
    private PhoneNumberValidator phoneNumberValidator;
    @Autowired
    private EmailValidator emailValidator;

    public void validate(CompanyContact contact) {
        if (contact.getGender() == null) {
            throw new BadRequestException("Contact ''{0}'': Gender can not be empty", contact.getFullname());
        }
        if (contact.getCompanyServiceTypes() == null || contact.getCompanyServiceTypes().isEmpty()) {
            throw new BadRequestException("Contact ''{0}'': Has to have at least one segment type", contact.getFullname());
        }
        if (contact.getCompanyLocation() == null) {
            throw new BadRequestException("Contact ''{0}'': Location can not be empty", contact.getFullname());
        }
        contact.getPhoneNumbers().forEach(phoneNumberValidator::validateForContact);
        contact.getEmails().forEach(emailValidator::validate);
    }
}
