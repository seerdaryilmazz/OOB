package ekol.agreement.domain.dto.agreement;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.agreement.domain.enumaration.*;
import ekol.agreement.domain.model.UnitPrice;
import ekol.agreement.service.QuoteService;
import ekol.agreement.util.BeanUtils;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnitPriceJson {

    private Long id;
    @NotNull(message = "Unit Price billing item can not be null")
    private BillingItemJson billingItem;
    @NotNull(message = "Unit Price service name can not be null")
    private String serviceName;
    @NotNull(message = "Unit Price price can not be null")
    private BigDecimal price;
    @NotNull(message = "Unit Price currency can not be null")
    private String currency;
    @NotNull(message = "Unit Price based on can not be null")
    private BasedOnType basedOn;
    private BigDecimal eurRef;
    private BigDecimal usdRef;
    private BigDecimal minimumWageRef;
    private Integer inflationRef;
    @NotNull(message = "Unit Price validity start date can not be null")
    private LocalDate validityStartDate;
    @NotNull(message = "Unit Price update period can not be null")
    private Integer updatePeriod;
    private RenewalDateType renewalDateType;
    private LocalDate validityEndDate;
    private PriceAdaptationModelJson priceModel;
    private String notes;
    private UtcDateTime lastUpdated;



    public UnitPrice toEntity(){
        return UnitPrice.builder()
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
                .priceModel(Objects.nonNull(getPriceModel()) ? getPriceModel().toEntity() : null)
                .notes(getNotes()).build();
    }

    public static UnitPriceJson fromEntity(UnitPrice unitPrice){
        return new UnitPriceJsonBuilder()
                .id(unitPrice.getId())
                .billingItem(BeanUtils.getBean(QuoteService.class).getBillingItemByName(unitPrice.getBillingItem(),true))
                .serviceName(unitPrice.getServiceName())
                .price(unitPrice.getPrice())
                .currency(unitPrice.getCurrency())
                .basedOn(unitPrice.getBasedOn())
                .eurRef(unitPrice.getEurRef())
                .usdRef(unitPrice.getUsdRef())
                .minimumWageRef(unitPrice.getMinimumWageRef())
                .inflationRef(unitPrice.getInflationRef())
                .validityStartDate(unitPrice.getValidityStartDate())
                .updatePeriod(unitPrice.getUpdatePeriod())
                .renewalDateType(unitPrice.getRenewalDateType())
                .validityEndDate(unitPrice.getValidityEndDate())
                .priceModel(PriceAdaptationModelJson.fromEntity(unitPrice.getPriceModel()))
                .notes(unitPrice.getNotes())
                .lastUpdated(unitPrice.getLastUpdated()).build();
    }
}
