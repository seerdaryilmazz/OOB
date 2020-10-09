package ekol.agreement.domain.dto.agreement;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.agreement.domain.enumaration.*;
import ekol.agreement.domain.model.HistoryUnitPrice;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HistoryUnitPriceJson {

    private Long id;
    private BillingItemJson billingItem;
    private String serviceName;
    private BigDecimal price;
    private String currency;
    private BasedOnType basedOn;
    private BigDecimal eurRef;
    private BigDecimal usdRef;
    private BigDecimal minimumWageRef;
    private Integer inflationRef;
    private LocalDate validityStartDate;
    private Integer updatePeriod;
    private RenewalDateType renewalDateType;
    private LocalDate validityEndDate;
    private PriceAdaptationModelJson priceModel;
    private String notes;
    private Long unitPriceId;
    private UtcDateTime lastUpdated;

    public HistoryUnitPrice toEntity(){
        return HistoryUnitPrice.builder()
                .id(getId())
                .billingItem(getBillingItem().getName())
                .serviceName(getServiceName())
                .price(getPrice())
                .currency(getCurrency())
                .basedOn(getBasedOn())
                .eurRef(getEurRef())
                .usdRef(getUsdRef())
                .minimumWageRef(getMinimumWageRef())
                .inflationRef(getInflationRef())
                .validityStartDate(getValidityStartDate())
                .updatePeriod(getUpdatePeriod())
                .renewalDateType(getRenewalDateType())
                .validityEndDate(getValidityEndDate())
                .priceModelId(Objects.nonNull(getPriceModel()) ? getPriceModel().getId() : null)
                .notes(getNotes())
                .unitPriceId(getUnitPriceId()).build();
    }
}
