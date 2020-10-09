package ekol.crm.quote.domain.model.quote;

import static java.util.stream.Collectors.toMap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.*;

import javax.persistence.*;

import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import ekol.crm.quote.client.KartoteksService;
import ekol.crm.quote.domain.annotation.QuoteService;
import ekol.crm.quote.domain.dto.*;
import ekol.crm.quote.domain.dto.quote.*;
import ekol.crm.quote.domain.enumaration.QuoteType;
import ekol.crm.quote.domain.model.*;
import ekol.crm.quote.domain.model.Package;
import ekol.crm.quote.domain.model.product.*;
import ekol.crm.quote.service.*;
import ekol.crm.quote.util.BeanUtils;
import ekol.model.IdNamePair;
import lombok.*;

@Entity
@Table(name = "CrmSpotQuote")
@PrimaryKeyJoinColumn(name = "quote_id")
@Getter
@Setter
@NoArgsConstructor
@Audited
@QuoteService(SpotQuoteService.class)
public class SpotQuote extends Quote{

    @Column
    private Long potentialId;

    @Column
    private String referrerTaskId;

    @Column(nullable = false)
    private LocalDate validityStartDate;

    @Column(nullable = false)
    private LocalDate validityEndDate;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="CONTACT_ID")),
            @AttributeOverride(name = "name", column=@Column(name="CONTACT_NAME"))})
    private IdNamePair contact;

    private Measurement measurement;

    @Column
    private BigDecimal payWeight;

    @Column
    private Integer quantity;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customsId")
    private Customs customs;

    @OneToMany(mappedBy="quote", fetch = FetchType.EAGER)
    @Where(clause="deleted=0")
    @JsonManagedReference
    private Set<Package> packages = new HashSet<>();

    @OneToMany(mappedBy="quote", fetch = FetchType.EAGER)
    @Where(clause="deleted=0")
    @JsonManagedReference
    private Set<Load> loads = new HashSet<>();

    @OneToMany(mappedBy="quote", fetch = FetchType.EAGER)
    @Where(clause="deleted=0")
    @JsonManagedReference
    private Set<VehicleRequirement> vehicleRequirements = new HashSet<>();

    @OneToMany(mappedBy="quote", fetch = FetchType.EAGER)
    @Where(clause="deleted=0")
    @JsonManagedReference
    private Set<ContainerRequirement> containerRequirements = new HashSet<>();

    @OneToMany(mappedBy="quote", fetch = FetchType.EAGER)
    @Where(clause="deleted=0")
    @JsonManagedReference
    private Set<Service> services = new HashSet<>();

    @OneToMany(mappedBy="quote", fetch = FetchType.EAGER)
    @Where(clause="deleted=0")
    @JsonManagedReference
    private Set<Price> prices = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paymentRuleId")
    private PaymentRule paymentRule;

    @Column
    private BigDecimal chargeableWeight;
    
    private PayWeightCalculation payWeightCalculation;

    public QuoteJson toJson(){
        SpotQuoteJson json = new SpotQuoteJson();
        json.setId(getId());
        json.setNumber(getNumber());
        json.setName(getName());
        json.setAccount(getAccount());
        json.setAccountLocation(getAccountLocation());
        json.setSubsidiary(getSubsidiary());
        json.setType(getType());
        json.setServiceArea(BeanUtils.getBean(KartoteksService.class).findServiceAreaByCode(getServiceArea(),true));
        json.setStatus(getStatus());
        json.setCreatedAt(getCreatedAt());
        json.setCreatedBy(getCreatedBy());
        json.setQuoteOwner(getQuoteOwner());
        json.setLastUpdatedBy(getLastUpdatedBy());
        json.setLastUpdated(getLastUpdated());
        json.setInitial(isInitial());
        json.setReferrerTaskId(getReferrerTaskId());
        json.setValidityStartDate(getValidityStartDate());
        json.setValidityEndDate(getValidityEndDate());
        json.setContact(getContact());
        json.setMeasurement(Optional.ofNullable(getMeasurement()).map(MeasurementJson::fromEntity).orElse(null));
        json.setPayWeight(getPayWeight());
        json.setQuantity(getQuantity());
        json.setProducts(Optional.ofNullable(getProducts()).map(Collection::stream).orElseGet(Stream::empty).map(Product::toJson).collect(Collectors.toList()));
        json.setCustoms(Optional.ofNullable(getCustoms()).map(CustomsJson::fromEntity).orElse(null));
        json.setPackages(Optional.ofNullable(getPackages()).map(Collection::stream).orElseGet(Stream::empty).map(PackageJson::fromEntity).collect(Collectors.toList()));
        json.setLoads(Optional.ofNullable(getLoads()).map(Collection::stream).orElseGet(Stream::empty).map(LoadJson::fromEntity).collect(Collectors.toList()));
        json.setVehicleRequirements(Optional.ofNullable(getVehicleRequirements()).map(Collection::stream).orElseGet(Stream::empty).map(VehicleRequirementJson::fromEntity).collect(Collectors.toList()));
        json.setContainerRequirements(Optional.ofNullable(getContainerRequirements()).map(Collection::stream).orElseGet(Stream::empty).map(ContainerRequirementJson::fromEntity).collect(Collectors.toList()));
        json.setServices(Optional.ofNullable(getServices()).map(Collection::stream).orElseGet(Stream::empty).map(ServiceJson::fromEntity).collect(Collectors.toList()));
        json.setPaymentRule(PaymentRuleJson.fromEntity(getPaymentRule()));
        json.setHoldingForCompanyTransfer(getHoldingForCompanyTransfer());
        json.setNotes(Optional.ofNullable(getNotes()).map(Collection::stream).orElseGet(Stream::empty).parallel().map(NoteJson::fromEntity).collect(Collectors.toList()));
        json.setDocuments(Optional.ofNullable(getDocuments()).map(Collection::stream).orElseGet(Stream::empty).parallel().map(DocumentJson::fromEntity).collect(Collectors.toList()));
        json.setMappedIds(getMappedIds().stream().collect(toMap(QuoteIdMapping::getApplication, QuoteIdMapping::getApplicationQuoteId)));
        json.setPrices(Optional.ofNullable(getPrices()).map(Collection::stream).orElseGet(Stream::empty).map(PriceJson::fromEntity).sorted(Comparator.comparing(priceJson -> priceJson.getBillingItem().getCode())).collect(Collectors.toList()));
        json.setDiscriminator(QuoteType.SPOT.name());
        json.setDefaultInvoiceCompanyCountry(getDefaultInvoiceCompanyCountry());
        json.setChargeableWeight(getChargeableWeight());
        json.setRequestedDate(getRequestedDate());
        json.getQuoteAttribute().putAll(getQuoteAttribute());
        json.getOrders().addAll(getOrders().stream().map(QuoteOrderMappingJson::fromEntity).filter(Objects::nonNull).collect(Collectors.toSet()));
        json.setPayWeightCalculation(Optional.ofNullable(payWeightCalculation).map(PayWeightCalculationJson::fromEntity).orElse(null));
        return json;
    }

    public void adjustName(){
        StringBuilder builder = new StringBuilder(getNumber() + " " + getType().name());
        if(!CollectionUtils.isEmpty(getProducts())){
            SpotProduct product = (SpotProduct)getProducts().iterator().next();
            builder.append(" From " + product.getFromCountry().getIso() + "-" +
                    product.getFromPoint().getName());
            builder.append(" To " + product.getToCountry().getIso() + "-" +
                    product.getToPoint().getName());
            builder.append(" @ " + product.getEarliestReadyDate());
        }
        setName(builder.toString());
    }
}
