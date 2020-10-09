package ekol.crm.quote.queue.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.*;

@JsonIgnoreProperties(value = {"newAccount"}, ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class AccountJson {
    private Long id;
    private String name;
    private IdNamePair company;
    private IsoNamePair country;
    private String accountOwner;
    private CodeNamePair accountType;
    private CodeNamePair segment;
    private String totalLogisticsPotential;
    private String strategicInformation;
}
