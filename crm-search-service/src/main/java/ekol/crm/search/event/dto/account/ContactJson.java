package ekol.crm.search.event.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class ContactJson {

    private Long id;
    private AccountJson account;
    private Long companyContactId;
    private String firstName;
    private String lastName;

}
