package ekol.crm.quote.domain.dto.kartoteksservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import lombok.Data;

import java.util.Set;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyContact {

    private Long id;

    private String firstName;

    private String lastName;

    private Set<EmailWithType> emails;

    private Boolean active;

    private CodeNamePair gender;

}
