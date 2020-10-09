package ekol.crm.history.domain.dto.type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.history.event.dto.quote.*;
import ekol.model.*;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quote {

    private Long id;
    private Long number;
    private String name;
    private String account;
    private String accountLocation;
    private String subsidiary;
    private String serviceArea;
    private String quoteType;
    private String status;

    // LONG TERM AND TENDER  QUOTE SPECIFIC FIELDS
    //BEGIN
    private LocalDate closeDate;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;
    private LocalDate operationStartDate;
    private String totalPrice;
    private String totalExpectedTurnover;
    //END

    // TENDER  QUOTE SPECIFIC FIELDS
    //BEGIN
    private LocalDate invitationDate;
    private String roundType;
    private Integer round;
    private String relatedPeople;
    //END

    // SPOT QUOTE SPECIFIC FIELDS
    //BEGIN
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
    private String contact;
    private BigDecimal totalWeight;
    private BigDecimal totalLdm;
    private BigDecimal totalVolume;
    private BigDecimal totalPayWeight;
    //END

    //SPOT PRODUCT
    //BEGIN
    private String fromCountry;
    private String fromPoint;
    private String toCountry;
    private String toPoint;
    private String shipmentLoadingType;
    private String incoterm;
    private LocalDate earliestReadyDate;
    private LocalDate latestReadyDate;
    private String loadingType;
    private String loadingCompany;
    private String loadingLocation;
    private String deliveryType;
    private String deliveryCompany;
    private String deliveryLocation;
    private Integer vehicleCount;
    //END

    //CUSTOMS
    //BEGIN
    private String departureClearanceResponsible;
    private String departureClearanceType;
    private String departureLocation;
    private String departureOffice;
    private String arrivalClearanceResponsible;
    private String arrivalClearanceType;
    private String arrivalLocation;
    private String arrivalOffice;
    //END

    private String loads;
    private String services;

    //PAYMENT RULE
    //BEGIN
    private String invoiceCompany;
    private String invoiceLocation;
    private String paymentType;
    private Integer paymentDueDays;
    //END

    public static Quote fromJson(QuoteJson json){
        Quote quote = new QuoteBuilder()
                .id(json.getId())
                .number(json.getNumber())
                .name(json.getName())
                .account(Optional.ofNullable(json.getAccount()).map(IdNamePair::getName).orElse(null))
                .accountLocation(Optional.ofNullable(json.getAccountLocation()).map(IdNamePair::getName).orElse(null))
                .subsidiary(Optional.ofNullable(json.getSubsidiary()).map(IdNamePair::getName).orElse(null))
                .serviceArea(Optional.ofNullable(json.getServiceArea()).map(CodeNamePair::getName).orElse(null))
                .quoteType(Optional.ofNullable(json.getType()).map(CodeNamePair::getName).orElse(null))
                .status(Optional.ofNullable(json.getStatus()).map(CodeNamePair::getName).orElse(null))
                .closeDate(json.getCloseDate())
                .contractStartDate(json.getContractStartDate())
                .contractEndDate(json.getContractEndDate())
                .operationStartDate(json.getOperationStartDate())
                .totalPrice(Optional.ofNullable(json.getTotalPrice())
                        .map(price -> price.getAmount() + " " + Optional.ofNullable(price.getCurrency())
                                .map(CodeNamePair::getName).orElse("")).orElse(null))
                .totalExpectedTurnover(Optional.ofNullable(json.getTotalExpectedTurnover())
                        .map(price -> price.getAmount() + " " + Optional.ofNullable(price.getCurrency())
                                .map(CodeNamePair::getName).orElse("")).orElse(null))
                .invitationDate(json.getInvitationDate())
                .roundType(Optional.ofNullable(json.getRoundType()).map(CodeNamePair::getName).orElse(null))
                .round(json.getRound())
                .relatedPeople(Optional.ofNullable(json.getRelatedPeople()).orElseGet(Collections::emptyList).stream()
                        .map(UserJson::getDisplayName).filter(StringUtils::isNotEmpty).sorted().collect(Collectors.joining(",")))
                .validityStartDate(json.getValidityStartDate())
                .validityEndDate(json.getValidityEndDate())
                .contact(Optional.ofNullable(json.getContact()).map(IdNamePair::getName).orElse(null))
                .totalWeight(Optional.ofNullable(json.getMeasurement()).map(MeasurementJson::getWeight).orElse(null))
                .totalLdm(Optional.ofNullable(json.getMeasurement()).map(MeasurementJson::getLdm).orElse(null))
                .totalVolume(Optional.ofNullable(json.getMeasurement()).map(MeasurementJson::getVolume).orElse(null))
                .totalPayWeight(json.getPayWeight())
                .loads(Optional.ofNullable(json.getLoads()).orElseGet(Collections::emptyList).stream()
                        .map(LoadJson::getType).map(CodeNamePair::getName).sorted().collect(Collectors.joining(",")))
                .services(Optional.ofNullable(json.getServices()).orElseGet(Collections::emptyList).stream()
                        .map(ServiceJson::getType).map(CodeNamePair::getName).sorted().collect(Collectors.joining(",")))
                .invoiceCompany(Optional.ofNullable(json.getPaymentRule()).map(PaymentRuleJson::getInvoiceCompany)
                        .map(IdNamePair::getName).orElse(null))
                .invoiceLocation(Optional.ofNullable(json.getPaymentRule()).map(PaymentRuleJson::getInvoiceLocation)
                        .map(IdNamePair::getName).orElse(null))
                .paymentType(Optional.ofNullable(json.getPaymentRule()).map(PaymentRuleJson::getType)
                        .map(CodeNamePair::getName).orElse(null))
                .paymentDueDays(Optional.ofNullable(json.getPaymentRule()).map(PaymentRuleJson::getPaymentDueDays).orElse(null)).build();


        // SET PRODUCT AND CUSTOMS FOR SPOT QUOTE
        if(json.getType().getCode().equals("SPOT")){
            if(!CollectionUtils.isEmpty(json.getProducts())){
                ProductJson productJson = json.getProducts().get(0);
                quote.setFromCountry(Optional.ofNullable(productJson.getFromCountry()).map(IsoNamePair::getName).orElse(null));
                quote.setFromPoint(Optional.ofNullable(productJson.getFromPoint()).map(IdNamePair::getName).orElse(null));
                quote.setToCountry(Optional.ofNullable(productJson.getToCountry()).map(IsoNamePair::getName).orElse(null));
                quote.setToPoint(Optional.ofNullable(productJson.getToPoint()).map(IdNamePair::getName).orElse(null));
                quote.setShipmentLoadingType(productJson.getShipmentLoadingType());
                quote.setIncoterm(productJson.getIncoterm());
                quote.setEarliestReadyDate(productJson.getEarliestReadyDate());
                quote.setLatestReadyDate(productJson.getLatestReadyDate());
                quote.setLoadingType(Optional.ofNullable(productJson.getLoadingType()).map(CodeNamePair::getName).orElse(null));
                quote.setLoadingCompany(Optional.ofNullable(productJson.getLoadingCompany()).map(IdNamePair::getName).orElse(null));
                quote.setLoadingLocation(Optional.ofNullable(productJson.getLoadingLocation()).map(IdNamePair::getName).orElse(null));
                quote.setDeliveryType(Optional.ofNullable(productJson.getDeliveryType()).map(CodeNamePair::getName).orElse(null));
                quote.setDeliveryCompany(Optional.ofNullable(productJson.getDeliveryCompany()).map(IdNamePair::getName).orElse(null));
                quote.setDeliveryLocation(Optional.ofNullable(productJson.getDeliveryLocation()).map(IdNamePair::getName).orElse(null));
                quote.setVehicleCount(productJson.getVehicleCount());
            }
            if(json.getCustoms() != null){
                CustomsPointJson departure = json.getCustoms().getDeparture();
                CustomsPointJson arrival = json.getCustoms().getArrival();
                if(departure != null){
                    quote.setDepartureClearanceResponsible(Optional.ofNullable(departure.getClearanceResponsible())
                            .map(CodeNamePair::getName).orElse(null));
                    quote.setDepartureClearanceType(Optional.ofNullable(departure.getClearanceType())
                            .map(CodeNamePair::getName).orElse(null));
                    quote.setDepartureLocation(Optional.ofNullable(departure.getLocation())
                            .map(IdNamePair::getName).orElse(null));
                    quote.setDepartureOffice(Optional.ofNullable(departure.getOffice())
                            .map(IdNamePair::getName).orElse(null));

                }
                if(arrival != null){
                    quote.setArrivalClearanceResponsible(Optional.ofNullable(arrival.getClearanceResponsible())
                            .map(CodeNamePair::getName).orElse(null));
                    quote.setArrivalClearanceType(Optional.ofNullable(arrival.getClearanceType())
                            .map(CodeNamePair::getName).orElse(null));
                    quote.setArrivalLocation(Optional.ofNullable(arrival.getLocation())
                            .map(IdNamePair::getName).orElse(null));
                    quote.setArrivalOffice(Optional.ofNullable(arrival.getOffice())
                            .map(IdNamePair::getName).orElse(null));

                }
            }
        }
        return quote;
    }
}
