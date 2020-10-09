package ekol.crm.account.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.account.domain.model.Contact;
import ekol.exceptions.ValidationException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class ContactJson {

    private Long id;
    private AccountJson account;
    private Long companyContactId;
    private String firstName;
    private String lastName;

    public Contact toEntity(){
        return  Contact.builder()
                .id(getId())
                .companyContactId(getCompanyContactId())
                .firstName(getFirstName())
                .lastName(getLastName()).build();
    }

    public static ContactJson fromEntity(Contact contact){
        return new ContactJsonBuilder()
                .id(contact.getId())
                .account(AccountJson.fromEntity(contact.getAccount()))
                .companyContactId(contact.getCompanyContactId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName()).build();
    }

    public void validate(){
        if(getCompanyContactId() == null){
            throw new ValidationException("Company contact should not be empty");
        }
        if(StringUtils.isBlank(getFirstName()) ||
                StringUtils.isBlank(getLastName())){
            throw new ValidationException("Contact name should not be empty");
        }
    }
}
