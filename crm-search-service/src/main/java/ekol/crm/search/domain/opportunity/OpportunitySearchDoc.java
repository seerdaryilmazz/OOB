package ekol.crm.search.domain.opportunity;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;

import ekol.crm.search.domain.MonetaryAmount;
import org.springframework.data.elasticsearch.annotations.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.*;

import ekol.crm.search.domain.SearchDoc;
import ekol.crm.search.event.dto.opportunity.OpportunityJson;
import ekol.crm.search.serializer.*;
import ekol.crm.search.utils.LanguageStringUtils;
import ekol.model.*;
import lombok.*;

/**
 * Created by Dogukan Sahinturk on 7.01.2020
 */
@Document(indexName = "crm", type = "opportunity")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpportunitySearchDoc extends SearchDoc {
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.String)
    private String name;

    @Field(type = FieldType.Long)
    private Long number;

    @Field(type = FieldType.Nested)
    private IdNamePair account;

    @Field(type = FieldType.Nested)
    private IdNamePair accountLocation;

    @Field(type = FieldType.String)
    private String accountOwner;

    @Field(type = FieldType.Nested)
    private IdNamePair ownerSubsidiary;

    @Field(type = FieldType.Nested)
    private CodeNamePair status;

    @Field(type = FieldType.Nested)
    private CodeNamePair serviceArea;

    @Field(type = FieldType.Nested)
    private MonetaryAmount expectedTurnoverPerYear;

    @Field(type = FieldType.Nested)
    private MonetaryAmount committedTurnoverPerYear;

    @Field(type = FieldType.Nested)
    private MonetaryAmount quotedTurnoverPerYear;

    @Field(type = FieldType.String)
    private String opportunityOwner;

    @Field(type = FieldType.String)
    private String createdBy;

    @Field(type = FieldType.String)
    private String lastUpdatedBy;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonDatePattern("dd/MM/yyyy")
    private Date expectedQuoteDate;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy HH:mm")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date createdAt;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy HH:mm")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date lastUpdated;

    public static OpportunitySearchDoc fromOpportunity(OpportunityJson opportunity){
        OpportunitySearchDoc opportunitySearchDoc = new OpportunitySearchDocBuilder()
                .id(opportunity.getId())
                .name(opportunity.getName())
                .number(opportunity.getNumber())
                .account(opportunity.getAccount())
                .accountLocation(opportunity.getAccountLocation())
                .accountOwner(opportunity.getAccountOwner())
                .ownerSubsidiary(opportunity.getOwnerSubsidiary())
                .status(opportunity.getStatus())
                .serviceArea(opportunity.getServiceArea())
                .opportunityOwner(opportunity.getOpportunityOwner())
                .createdBy(opportunity.getCreatedBy())
                .expectedTurnoverPerYear(MonetaryAmount.fromMonetaryAmount(opportunity.getExpectedTurnoverPerYear()))
                .quotedTurnoverPerYear(MonetaryAmount.fromMonetaryAmount(opportunity.getQuotedTurnoverPerYear()))
                .committedTurnoverPerYear(MonetaryAmount.fromMonetaryAmount(opportunity.getCommittedTurnoverPerYear()))
                .lastUpdatedBy(opportunity.getLastUpdatedBy())
                .expectedQuoteDate(Objects.nonNull(opportunity.getExpectedQuoteDate()) ? Date.from(opportunity.getExpectedQuoteDate().atStartOfDay().toInstant(ZoneOffset.UTC)) : null)
                .createdAt(Date.from(opportunity.getCreatedAt().getDateTime().toInstant(ZoneOffset.UTC)))
                .lastUpdated(Date.from(opportunity.getLastUpdated().getDateTime().toInstant(ZoneOffset.UTC)))
                .build();
        opportunitySearchDoc.setAccountName(LanguageStringUtils.setTextForSearch(opportunity.getAccount().getName()));
        opportunitySearchDoc.setTextToSearch(LanguageStringUtils.setTextForSearch(opportunity.getName()));
        opportunitySearchDoc.setDocumentType("opportunity");
        return opportunitySearchDoc;

    }
}
