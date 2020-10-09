package ekol.outlook.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.outlook.model.OutlookMail;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class UserJson {

    private String emailAddress;
    private String name;

    public OutlookMail.User toOutlookUser(){
        return OutlookMail.User.builder()
                .emailAddress(OutlookMail.EmailAddress.builder()
                        .address(getEmailAddress())
                        .name(getName()).build())
                .build();
    }
}
