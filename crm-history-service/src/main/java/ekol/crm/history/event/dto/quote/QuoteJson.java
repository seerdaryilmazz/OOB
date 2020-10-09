package ekol.crm.history.event.dto.quote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import ekol.mongodb.domain.datetime.UtcDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class QuoteJson {

    private Long id;
    private Long number;
    private String name;
    private IdNamePair account;
    private IdNamePair accountLocation;
    private IdNamePair subsidiary;
    private CodeNamePair serviceArea;
    private CodeNamePair type;
    private List<ProductJson> products;
    private CodeNamePair status;
    private String createdBy;
    private UtcDateTime createdAt;
    private String lastUpdatedBy;
    private UtcDateTime lastUpdated;

    // SPOT QUOTE SPECIFIC FIELDS
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
    private IdNamePair contact;
    private MeasurementJson measurement;
    private BigDecimal payWeight;
    private CustomsJson customs;
    private List<LoadJson> loads;
    private List<ServiceJson> services;
    private PaymentRuleJson paymentRule;

    // LONG TERM AND TENDER  QUOTE SPECIFIC FIELDS
    private LocalDate closeDate;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private LocalDate operationStartDate;
    private MonetaryAmountJson totalPrice;
    private MonetaryAmountJson totalExpectedTurnover;

    // TENDER  QUOTE SPECIFIC FIELDS
    private LocalDate invitationDate;
    private CodeNamePair roundType;
    private Integer round;
    private List<UserJson> relatedPeople;
}

