package ekol.crm.search.domain.account;

import org.springframework.data.elasticsearch.annotations.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.search.domain.SearchDoc;
import ekol.crm.search.event.dto.account.ContactJson;
import ekol.crm.search.utils.LanguageStringUtils;
import ekol.model.IdNamePair;
import lombok.*;


@Document(indexName = "crm", type = "contact")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactSearchDoc extends SearchDoc {

    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Nested)
    private IdNamePair account;

    @Field(type = FieldType.Long)
    private Long companyContactId;

    @Field(type = FieldType.String)
    private String firstName;

    @Field(type = FieldType.String)
    private String lastName;

    public static ContactSearchDoc fromContact(ContactJson contact){
        ContactSearchDoc contactSearchDoc = new ContactSearchDocBuilder()
                .id(contact.getId())
                .account(IdNamePair.with(contact.getAccount().getId(), contact.getAccount().getName()))
                .companyContactId(contact.getCompanyContactId())
                .firstName(contact.getFirstName())
                .lastName(contact.getLastName()).build();

        contactSearchDoc.setAccountName(LanguageStringUtils.setTextForSearch(contact.getAccount().getName()));
        contactSearchDoc.setTextToSearch(LanguageStringUtils.setTextForSearch(contact.getFirstName() + " " + contact.getLastName()));
        contactSearchDoc.setDocumentType("contact");
        return contactSearchDoc;
    }

}
