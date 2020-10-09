package ekol.crm.history.event.dto.opportunity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.history.event.dto.quote.MonetaryAmountJson;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import ekol.mongodb.domain.datetime.UtcDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Dogukan Sahinturk on 22.11.2019
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class OpportunityJson {
    private Long id;
    private Long number;
    private CodeNamePair status;
    private CodeNamePair serviceArea;
    private String opportunityOwner;
    private UtcDateTime createdAt;
    private String createdBy;
    private String name;
    private IdNamePair account;
    private IdNamePair accountLocation;
    private IdNamePair ownerSubsidiary;
    private MonetaryAmountJson expectedTurnoverPerYear;
    private MonetaryAmountJson actualTurnoverPerYear;
    private String lastUpdatedBy;
}
