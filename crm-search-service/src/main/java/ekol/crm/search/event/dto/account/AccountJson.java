package ekol.crm.search.event.dto.account;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import ekol.model.IsoNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private List<ContactJson> contacts = new ArrayList<>();
    private String city;
    private String district;
}
