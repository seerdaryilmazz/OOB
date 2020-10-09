package ekol.crm.search.domain.account;

import java.time.ZoneOffset;
import java.util.*;

import org.springframework.data.elasticsearch.annotations.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.*;

import ekol.crm.search.domain.*;
import ekol.crm.search.event.dto.account.*;
import ekol.crm.search.serializer.*;
import ekol.crm.search.utils.LanguageStringUtils;
import ekol.model.*;
import lombok.*;


@Document(indexName = "crm", type = "account")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountSearchDoc extends SearchDoc {

    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.String)
    private String name;

    @Field(type = FieldType.Nested)
    private CompanySearchDoc company;

    @Field(type = FieldType.Nested)
    private IsoNamePair country;

    @Field(type = FieldType.String)
    private String accountOwner;

    @Field(type = FieldType.Nested)
    private CodeNamePair accountType;

    @Field(type = FieldType.Nested)
    private CodeNamePair segment;

    @Field(type = FieldType.Nested)
    private CodeNamePair parentSector;

    @Field(type = FieldType.Nested)
    private CodeNamePair subSector;

    @Field(type = FieldType.Nested)
    private AccountDetailJson details;

    @Field(type = FieldType.String)
    private String createdBy;

    @Field(type = FieldType.String)
    private String lastUpdatedBy;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy HH:mm")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date lastUpdated;
    
    @Field(type = FieldType.String)
    private String city;
    
    @Field(type = FieldType.String)
    private String district;
    
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "dd/MM/yyyy HH:mm")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    private Date createdAt;
    
    public static AccountSearchDoc fromAccount(AccountJson account){
        AccountSearchDoc accountSearchDoc = new AccountSearchDocBuilder()
                .id(account.getId())
                .name(account.getName())
                .company(CompanySearchDoc.fromCompany(account.getCompany()))
                .country(account.getCountry())
                .accountOwner(account.getAccountOwner())
                .accountType(account.getAccountType())
                .segment(account.getSegment())
                .parentSector(account.getParentSector())
                .subSector(account.getSubSector())
                .details(Optional.ofNullable(account.getDetails()).orElse(null))
                .createdBy(account.getCreatedBy())
                .lastUpdatedBy(account.getLastUpdatedBy())
                .lastUpdated(Date.from(account.getLastUpdated().getDateTime().toInstant(ZoneOffset.UTC)))
                .createdAt(Date.from(account.getCreatedAt().getDateTime().toInstant(ZoneOffset.UTC)))
                .city(account.getCity())
                .district(account.getDistrict())
                .build();

        accountSearchDoc.setAccountName(LanguageStringUtils.setTextForSearch(account.getName()));
        accountSearchDoc.setTextToSearch(LanguageStringUtils.setTextForSearch(account.getName()));
        accountSearchDoc.setDocumentType("account");
        return accountSearchDoc;
    }
}
