package ekol.crm.quote.domain.model.quote;

import static java.util.stream.Collectors.toMap;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.*;

import javax.persistence.*;

import org.hibernate.envers.Audited;

import ekol.crm.quote.client.KartoteksService;
import ekol.crm.quote.domain.annotation.QuoteService;
import ekol.crm.quote.domain.dto.*;
import ekol.crm.quote.domain.dto.quote.*;
import ekol.crm.quote.domain.enumaration.*;
import ekol.crm.quote.domain.model.*;
import ekol.crm.quote.domain.model.product.Product;
import ekol.crm.quote.service.*;
import ekol.crm.quote.util.BeanUtils;
import ekol.model.CodeNamePair;
import lombok.*;

@Entity
@Table(name = "CrmTenderQuote")
@PrimaryKeyJoinColumn(name = "quote_id", referencedColumnName = "id")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
@QuoteService(TenderQuoteService.class)
public class TenderQuote extends Quote{

    @Column
    private LocalDate closeDate;

    @Column
    private LocalDate contractStartDate;

    @Column
    private LocalDate contractEndDate;

    @Column
    private LocalDate operationStartDate;

    @Column
    private LocalDate invitationDate;

    @Enumerated(EnumType.STRING)
    private RoundType roundType;

    @Column
    private Integer round;

    @Embedded
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "CrmQuoteUser", joinColumns = @JoinColumn(name = "quoteId"))
	@AttributeOverrides({
		@AttributeOverride(name = "code", column = @Column(name = "code")),
		@AttributeOverride(name = "name", column = @Column(name = "name"))
	})
    private Set<CodeNamePair> relatedPeople = new HashSet<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "priceAmount")),
            @AttributeOverride(name = "currency", column = @Column(name = "priceCurrency"))
    })
    private MonetaryAmount totalPrice;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "expTurnoverAmount")),
            @AttributeOverride(name = "currency", column = @Column(name = "expTurnoverCurrency"))
    })
    private MonetaryAmount totalExpectedTurnover;

    @Column
    private Integer paymentDueDays;

    @Column
    private String paymentDescription;

    @Column
    private String productType;

    @Column
    private String transportationType;

    @Column
    private String equipmentType;

    @Column
    private String importantPriceIssues;

    @Column
    private String conversionFactorsLtl;

    @Column
    private String dieselMechanism;

    @Column
    private String adrFrigoExpress;

    @Column
    private String demurrage;

    @Column
    private String cancellation;

    @Column
    private String kpi;

    @Column
    private String penaltyDetail;

    @Column
    private String loadUnloadFreeTimes;

    @Column
    private String stackability;

    public QuoteJson toJson(){
        TenderQuoteJson json = new TenderQuoteJson();
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
        json.setCloseDate(getCloseDate());
        json.setContractStartDate(getContractStartDate());
        json.setContractEndDate(getContractEndDate());
        json.setOperationStartDate(getOperationStartDate());
        json.setInvitationDate(getInvitationDate());
        json.setRoundType(getRoundType());
        json.setRound(getRound());
        json.setRelatedPeople(getRelatedPeople());
        json.setTotalPrice(Optional.ofNullable(getTotalPrice()).map(MonetaryAmountJson::fromEntity).orElse(null));
        json.setTotalExpectedTurnover(Optional.ofNullable(getTotalExpectedTurnover()).map(MonetaryAmountJson::fromEntity).orElse(null));
        json.setPaymentDueDays(getPaymentDueDays());
        json.setPaymentDescription(getPaymentDescription());
        json.setProductType(getProductType());
        json.setTransportationType(getTransportationType());
        json.setEquipmentType(getEquipmentType());
        json.setImportantPriceIssues(getImportantPriceIssues());
        json.setConversionFactorsLtl(getConversionFactorsLtl());
        json.setDieselMechanism(getDieselMechanism());
        json.setAdrFrigoExpress(getAdrFrigoExpress());
        json.setDemurrage(getDemurrage());
        json.setCancellation(getCancellation());
        json.setKpi(getKpi());
        json.setPenaltyDetail(getPenaltyDetail());
        json.setLoadUnloadFreeTimes(getLoadUnloadFreeTimes());
        json.setStackability(getStackability());
        json.setProducts(Optional.ofNullable(getProducts()).map(Collection::stream).orElseGet(Stream::empty).map(Product::toJson).collect(Collectors.toList()));
        json.setNotes(Optional.ofNullable(getNotes()).map(Collection::stream).orElseGet(Stream::empty).map(NoteJson::fromEntity).parallel().collect(Collectors.toList()));
        json.setDocuments(Optional.ofNullable(getDocuments()).map(Collection::stream).orElseGet(Stream::empty).map(DocumentJson::fromEntity).parallel().collect(Collectors.toList()));
        json.setMappedIds(getMappedIds().parallelStream().collect(toMap(QuoteIdMapping::getApplication, QuoteIdMapping::getApplicationQuoteId)));
        json.setDiscriminator(QuoteType.TENDER.name());
        json.setDefaultInvoiceCompanyCountry(getDefaultInvoiceCompanyCountry());
        json.setHoldingForCompanyTransfer(getHoldingForCompanyTransfer());
        json.setRequestedDate(getRequestedDate());
        json.getQuoteAttribute().putAll(getQuoteAttribute());
        json.getOrders().addAll(getOrders().stream().map(QuoteOrderMappingJson::fromEntity).filter(Objects::nonNull).collect(Collectors.toSet()));
        return json;
    }

    public void adjustName(){}
}
