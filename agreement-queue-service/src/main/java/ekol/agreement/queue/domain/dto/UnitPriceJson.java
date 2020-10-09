package ekol.agreement.queue.domain.dto;

import ekol.model.CodeNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class UnitPriceJson {
    private Long id;
    private BillingItemJson billingItem;
    private String serviceName;
    private BigDecimal price;
    private String currency;
    private CodeNamePair basedOn;
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
}
