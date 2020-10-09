package ekol.crm.history.event.dto.agreement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class UnitPriceJson {
    private Long id;
    private BillingItemJson billingItem;
    private String serviceName;
    private BigDecimal price;
    private String currency;
    private CodeNamePair basedOn;
    private BigDecimal eurRef;
    private BigDecimal usdRef;
    private BigDecimal minimumWageRef;
    private Integer inflationRef;
    private LocalDate validityStartDate;
    private Integer updatePeriod;
    private CodeNamePair renewalDateType;
    private LocalDate validityEndDate;
    private PriceAdaptationModelJson priceModel;
    private String notes;
}
