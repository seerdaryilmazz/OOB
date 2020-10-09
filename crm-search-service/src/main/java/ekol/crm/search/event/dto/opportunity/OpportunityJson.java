package ekol.crm.search.event.dto.opportunity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.search.event.dto.quote.ProductJson;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dogukan Sahinturk on 15.01.2020
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
    private String accountOwner;
    private IdNamePair ownerSubsidiary;
    private MonetaryAmountJson expectedTurnoverPerYear;
    private MonetaryAmountJson committedTurnoverPerYear;
    private MonetaryAmountJson quotedTurnoverPerYear;
    private List<ProductJson> products;
    private Map<String, String> opportunityAttribute = new HashMap<>();
    private String lastUpdatedBy;
    private UtcDateTime lastUpdated;
    private LocalDate expectedQuoteDate;
}
