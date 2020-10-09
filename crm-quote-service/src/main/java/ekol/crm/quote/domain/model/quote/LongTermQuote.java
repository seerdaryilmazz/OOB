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
import ekol.crm.quote.domain.dto.businessVolume.BusinessVolumeJson;
import ekol.crm.quote.domain.dto.quote.*;
import ekol.crm.quote.domain.enumaration.QuoteType;
import ekol.crm.quote.domain.model.*;
import ekol.crm.quote.domain.model.businessVolume.BusinessVolume;
import ekol.crm.quote.domain.model.product.Product;
import ekol.crm.quote.service.*;
import ekol.crm.quote.util.BeanUtils;
import lombok.*;

@Entity
@Table(name = "CrmLongTermQuote")
@PrimaryKeyJoinColumn(name = "quote_id", referencedColumnName = "id")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Audited
@QuoteService(LongTermQuoteService.class)
public class LongTermQuote extends Quote{

    @Column
    private LocalDate closeDate;

    @Column
    private LocalDate contractStartDate;

    @Column
    private LocalDate contractEndDate;

    @Column
    private LocalDate operationStartDate;
    
    @OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "businessVolumeId")
    private BusinessVolume businessVolume;

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

    public QuoteJson toJson(){
        LongTermQuoteJson json = new LongTermQuoteJson();
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
        json.setProducts(Optional.ofNullable(getProducts()).map(Collection::stream).orElseGet(Stream::empty).map(Product::toJson).collect(Collectors.toList()));
        json.setTotalPrice(getTotalPrice() != null ? MonetaryAmountJson.fromEntity(getTotalPrice()): null);
        json.setTotalExpectedTurnover(getTotalExpectedTurnover() != null ? MonetaryAmountJson.fromEntity(getTotalExpectedTurnover()): null);
        json.setNotes(Optional.ofNullable(getNotes()).map(Collection::stream).orElseGet(Stream::empty).parallel().map(NoteJson::fromEntity).collect(Collectors.toList()));
        json.setDocuments(Optional.ofNullable(getDocuments()).map(Collection::stream).orElseGet(Stream::empty).parallel().map(DocumentJson::fromEntity).collect(Collectors.toList()));
        json.setMappedIds(getMappedIds().stream().collect(toMap(QuoteIdMapping::getApplication, QuoteIdMapping::getApplicationQuoteId)));
        json.setDiscriminator(QuoteType.LONG_TERM.name());
        json.setDefaultInvoiceCompanyCountry(getDefaultInvoiceCompanyCountry());
        json.setHoldingForCompanyTransfer(getHoldingForCompanyTransfer());
        json.setRequestedDate(getRequestedDate());
        json.getQuoteAttribute().putAll(getQuoteAttribute());
        json.getOrders().addAll(getOrders().stream().map(QuoteOrderMappingJson::fromEntity).filter(Objects::nonNull).collect(Collectors.toSet()));
        json.setBusinessVolume(BusinessVolumeJson.fromEntity(getBusinessVolume()));
        return json;
    }

    public void adjustName(){}
}
