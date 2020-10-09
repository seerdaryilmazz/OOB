package ekol.crm.quote.queue.common.dto;

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
    private Long potentialId;
    private String referrerTaskId;
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
    private IdNamePair contact;
    private MeasurementJson measurement;
    private BigDecimal payWeight;
    private Integer quantity;
    private CustomsJson customs;
    private List<PackageJson> packages;
    private List<LoadJson> loads;
    private List<VehicleRequirementJson> vehicleRequirements;
    private List<ContainerRequirementJson> containerRequirements;
    private List<ServiceJson> services;
    private List<PriceJson> prices;
    private PaymentRuleJson paymentRule;
    private Boolean holdingForCompanyTransfer;
    private BigDecimal chargeableWeight;

}

