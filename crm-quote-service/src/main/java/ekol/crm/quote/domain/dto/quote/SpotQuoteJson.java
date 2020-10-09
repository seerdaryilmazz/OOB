package ekol.crm.quote.domain.dto.quote;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.*;

import org.springframework.util.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.domain.dto.*;
import ekol.crm.quote.domain.dto.product.ProductJson;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.domain.model.quote.*;
import ekol.exceptions.ValidationException;
import ekol.model.IdNamePair;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpotQuoteJson extends QuoteJson{

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
    private BigDecimal chargeableWeight;
    private PayWeightCalculationJson payWeightCalculation;

    public Quote toEntity(){
        SpotQuote entity = new SpotQuote();
        entity.setId(getId());
        entity.setNumber(getNumber());
        entity.setName(getName());
        entity.setAccount(getAccount());
        entity.setAccountLocation(getAccountLocation());
        entity.setSubsidiary(getSubsidiary());
        entity.setType(getType());
        entity.setServiceArea(getServiceArea().getCode());
        entity.setStatus(getStatus());
        entity.setCreatedAt(getCreatedAt());
        entity.setCreatedBy(getCreatedBy());
        entity.setQuoteOwner(getQuoteOwner());
        entity.setReferrerTaskId(getReferrerTaskId());
        entity.setValidityStartDate(getValidityStartDate());
        entity.setValidityEndDate(getValidityEndDate());
        entity.setContact(getContact());
        entity.setMeasurement(getMeasurement() != null ? getMeasurement().toEntity() : null);
        entity.setPayWeight(getPayWeight());
        entity.setQuantity((getQuantity()));
        entity.setProducts(Optional.ofNullable(getProducts()).map(Collection::stream).orElseGet(Stream::empty).map(ProductJson::toEntity).collect(Collectors.toSet()));
        entity.setCustoms(getCustoms() != null ? getCustoms().toEntity() : null);
        entity.setPackages(Optional.ofNullable(getPackages()).map(Collection::stream).orElseGet(Stream::empty).map(PackageJson::toEntity).collect(Collectors.toSet()));
        entity.setLoads(Optional.ofNullable(getLoads()).map(Collection::stream).orElseGet(Stream::empty).map(LoadJson::toEntity).collect(Collectors.toSet()));
        entity.setVehicleRequirements(Optional.ofNullable(getVehicleRequirements()).map(Collection::stream).orElseGet(Stream::empty).map(VehicleRequirementJson::toEntity).collect(Collectors.toSet()));
        entity.setContainerRequirements(Optional.ofNullable(getContainerRequirements()).map(Collection::stream).orElseGet(Stream::empty).map(ContainerRequirementJson::toEntity).collect(Collectors.toSet()));
        entity.setServices(Optional.ofNullable(getServices()).map(Collection::stream).orElseGet(Stream::empty).map(ServiceJson::toEntity).collect(Collectors.toSet()));
        entity.setPrices(Optional.ofNullable(getPrices()).map(Collection::stream).orElseGet(Stream::empty).map(PriceJson::toEntity).collect(Collectors.toSet()));
        entity.setPaymentRule(getPaymentRule() != null ? getPaymentRule().toEntity(): null);
        entity.setHoldingForCompanyTransfer(getHoldingForCompanyTransfer());
        entity.setNotes(Optional.ofNullable(getNotes()).map(Collection::stream).orElseGet(Stream::empty).map(NoteJson::toEntity).collect(Collectors.toList()));
        entity.setDocuments(Optional.ofNullable(getDocuments()).map(Collection::stream).orElseGet(Stream::empty).map(DocumentJson::toEntity).collect(Collectors.toList()));
        entity.setDefaultInvoiceCompanyCountry(getDefaultInvoiceCompanyCountry());
        entity.setPdfLanguage(getPdfLanguage());
        entity.setChargeableWeight(getChargeableWeight());
        entity.setRequestedDate(getRequestedDate());
        entity.getQuoteAttribute().putAll(getQuoteAttribute());
        entity.getOrders().addAll(getOrders().stream().map(QuoteOrderMappingJson::toEntity).collect(Collectors.toSet()));
        entity.setPayWeightCalculation(Optional.ofNullable(payWeightCalculation).map(PayWeightCalculationJson::toEntity).orElse(null));
        
        if(getStatus() == QuoteStatus.CLOSED){
            if(CollectionUtils.isEmpty(getProducts())) {
                throw new ValidationException("Product should be entered");
            }
            if(getProducts().size() != 1){
                throw new ValidationException("Spot quote should have only one product");
            }
            ProductStatus productStatus = getProducts().get(0).getStatus();
            if(productStatus == null){
                throw new ValidationException("Product should have status");
            }
            if (productStatus == ProductStatus.WON) {
                entity.setStatus(QuoteStatus.WON);
            }else if (productStatus == ProductStatus.LOST){
                entity.setStatus(QuoteStatus.LOST);
            }
            else if(productStatus==ProductStatus.PDF_CREATED) {
            	entity.setStatus(QuoteStatus.PDF_CREATED);
            }
            else {
            	entity.setStatus(QuoteStatus.CANCELED);
            }
        }
        return entity;
    }

    public void validate(QuoteType type){
        if(getValidityStartDate() == null){
            throw new ValidationException("Validity start date should not be empty");
        }
        if(getValidityEndDate() == null){
            throw new ValidationException("Validity end date should not be empty");
        }
        if(getValidityEndDate().isBefore(getValidityStartDate())){
            throw new ValidationException("Validity end date should not be before than validity start date");
        }
        if(getContact() == null || getContact().getId() == null){
            throw new ValidationException("Contact should not be empty");
        }
        if(StringUtils.isEmpty(getProducts())){
            throw new ValidationException("Quote should have a product");
        }
        if(getProducts().size() != 1){
            throw new ValidationException("Spot quote should have only one product");
        }
        getProducts().iterator().next().validateProduct(type, getDefaultInvoiceCompanyCountry());

        if(getPaymentRule() == null){
            throw new ValidationException("Quote should have payment rules");
        }
        getPaymentRule().validate();
    }
}

