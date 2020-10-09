package ekol.crm.history.event.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import ekol.model.IsoNamePair;
import ekol.mongodb.domain.datetime.UtcDateTime;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class AccountJson {
    private Long id;
    private String name;
    private IdNamePair company;
    private IsoNamePair country;
    private String accountOwner;
    private CodeNamePair accountType;
    private CodeNamePair segment;
    private CodeNamePair parentSector;
    private CodeNamePair subSector;
    private AccountDetailJson details;
    private UtcDateTime createdAt;
    private String createdBy;
    private String lastUpdatedBy;
    private UtcDateTime lastUpdated;
}
