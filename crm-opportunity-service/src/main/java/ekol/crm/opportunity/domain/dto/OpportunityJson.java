package ekol.crm.opportunity.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.opportunity.domain.dto.product.ProductJson;
import ekol.crm.opportunity.domain.enumaration.Currency;
import ekol.crm.opportunity.domain.model.MonetaryAmount;
import ekol.crm.opportunity.domain.model.product.Product;
import ekol.hibernate5.domain.embeddable.UtcDateTime;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import ekol.crm.opportunity.domain.enumaration.OpportunityStatus;
import ekol.crm.opportunity.domain.model.Opportunity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Dogukan Sahinturk on 18.11.2019
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpportunityJson {
    private Long id;
    private Long number;
    @NotNull(message = "Status can not be null")
    private OpportunityStatus status;
    @NotNull(message = "Service area can not be null")
    private CodeNamePair serviceArea;
    @NotNull(message = "Opportunity owner can not be null")
    private String opportunityOwner;
    private UtcDateTime createdAt;
    private String createdBy;
    @NotNull(message = "Opportunity name can not be null")
    private String name;
    @NotNull(message = "Account can not be null")
    private IdNamePair account;
    @NotNull(message = "Account location can not be null")
    private IdNamePair accountLocation;
    @NotNull(message = "Owner subsidiary can not be null")
    private IdNamePair ownerSubsidiary;
    @NotNull(message = "Expected turnover per year can not be null")
    private MonetaryAmountJson expectedTurnoverPerYear;
    private MonetaryAmountJson committedTurnoverPerYear;
    private MonetaryAmountJson quotedTurnoverPerYear;
    private List<ProductJson> products;
    private Map<String, String> opportunityAttribute = new HashMap<>();
    private String lastUpdatedBy;
    private UtcDateTime lastUpdated;
    private CloseReasonJson closeReason;
    private LocalDate expectedQuoteDate;
    private List<DocumentJson> documents = new ArrayList<>();
    private List<NoteJson> notes  = new ArrayList<>();

    public Opportunity toEntity(){
        Opportunity entity = new Opportunity();
        entity.setId(getId());
        entity.setNumber(getNumber());
        entity.setStatus(getStatus());
        entity.setServiceArea(getServiceArea());
        entity.setOpportunityOwner(getOpportunityOwner());
        entity.setCreatedAt(getCreatedAt());
        entity.setCreatedBy(getCreatedBy());
        entity.setName(getName());
        entity.setAccount(getAccount());
        entity.setAccountLocation(getAccountLocation());
        entity.setOwnerSubsidiary(getOwnerSubsidiary());
        entity.setExpectedTurnoverPerYear(Optional.ofNullable(getExpectedTurnoverPerYear()).map(MonetaryAmountJson::toEntity).orElse(MonetaryAmount.createDefault()));
        entity.setCommittedTurnoverPerYear(Optional.ofNullable(getCommittedTurnoverPerYear()).map(MonetaryAmountJson::toEntity).orElse(MonetaryAmount.createDefault()));
        entity.setQuotedTurnoverPerYear(Optional.ofNullable(getQuotedTurnoverPerYear()).map(MonetaryAmountJson::toEntity).orElse(MonetaryAmount.createDefault()));
        entity.setExpectedQuoteDate(getExpectedQuoteDate());
        entity.setProducts(!CollectionUtils.isEmpty(getProducts()) ? getProducts().stream().map(ProductJson::toEntity).collect(Collectors.toSet()) : null);
        entity.setCloseReason(Optional.ofNullable(getCloseReason()).map(CloseReasonJson::toEntity).orElse(null));
        Optional.ofNullable(getOpportunityAttribute()).ifPresent(entity.getOpportunityAttribute()::putAll);
        return entity;
    }

    public static OpportunityJson fromEntity(Opportunity opportunity){
        return new OpportunityJsonBuilder()
                .id(opportunity.getId())
                .number(opportunity.getNumber())
                .status(opportunity.getStatus())
                .serviceArea(opportunity.getServiceArea())
                .opportunityOwner(opportunity.getOpportunityOwner())
                .createdAt(opportunity.getCreatedAt())
                .createdBy(opportunity.getCreatedBy())
                .name(opportunity.getName())
                .account(opportunity.getAccount())
                .accountLocation(opportunity.getAccountLocation())
                .ownerSubsidiary(opportunity.getOwnerSubsidiary())
                .expectedTurnoverPerYear(MonetaryAmountJson.fromEntity(opportunity.getExpectedTurnoverPerYear()))
                .committedTurnoverPerYear(MonetaryAmountJson.fromEntity(opportunity.getCommittedTurnoverPerYear()))
                .quotedTurnoverPerYear(MonetaryAmountJson.fromEntity(opportunity.getQuotedTurnoverPerYear()))
                .expectedQuoteDate(opportunity.getExpectedQuoteDate())
                .lastUpdatedBy(opportunity.getLastUpdatedBy())
                .lastUpdated(opportunity.getLastUpdated())
                .notes(!CollectionUtils.isEmpty(opportunity.getNotes()) ? opportunity.getNotes().stream().map(NoteJson::fromEntity).collect(Collectors.toList()) : null)
                .documents(!CollectionUtils.isEmpty(opportunity.getDocuments()) ? opportunity.getDocuments().stream().map(DocumentJson::fromEntity).collect(Collectors.toList()) : null)
                .products(!CollectionUtils.isEmpty(opportunity.getProducts()) ? opportunity.getProducts().stream().map(Product::toJson).collect(Collectors.toList()) : null)
                .closeReason(Optional.ofNullable(opportunity.getCloseReason()).map(CloseReasonJson::fromEntity).orElse(null))
                .opportunityAttribute(opportunity.getOpportunityAttribute())
                .build();
    }
}
