package ekol.kartoteks.domain.search;

import ekol.kartoteks.domain.Company;
import ekol.kartoteks.utils.LanguageStringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

/**
 * Created by kilimci on 25/03/16.
 */
@Document(indexName = "company")
@Getter
@Setter
@Setting(settingPath = "elastic/searchSettings.json")
@NoArgsConstructor
@AllArgsConstructor
public class CompanySearchDoc {

    @Field(type = FieldType.Long)
    private Long id;
    @MultiField(mainField = @Field(type = FieldType.String),
            otherFields = {@InnerField(suffix = "raw", type = FieldType.String, index = FieldIndex.not_analyzed)})
    private String name;
    @MultiField(
            mainField = @Field(includeInParent = true, type = FieldType.String, analyzer = "search_analyzer", searchAnalyzer = "search_analyzer"),
            otherFields = {
                    @InnerField(suffix = "raw", type = FieldType.String, index = FieldIndex.analyzed, indexAnalyzer = "autocomplete_analyzer", searchAnalyzer = "autocomplete_analyzer")})
    private String nameToSearch;
    @Field(type = FieldType.String)
    private String shortName;
    @Field(type = FieldType.String)
    private String countryCode;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String countryName;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String taxOffice;
    @Field(type = FieldType.String)
    private String taxOfficeCode;
    @Field(type = FieldType.String)
    private String taxId;
    @Field(type = FieldType.String)
    private String companyType;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String logoUrl;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String logoFilePath;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String website;
    @Field(type = FieldType.Nested)
    private List<CompanyLocationSearchDoc> locations = new ArrayList<>();
    @Field(type = FieldType.Boolean)
    private boolean deletable;
    @Field(type = FieldType.Boolean)
    private boolean shortNameChecked;
    @Field(type = FieldType.Integer)
    private Integer statsCustomer;
    @Field(type = FieldType.Integer)
    private Integer statsParticipant;
    @Transient
    private Float score;

    public static CompanySearchDoc fromCompany(Company company){
        CompanySearchDoc doc = new CompanySearchDoc();
        doc.setName(company.getName());
        doc.setNameToSearch(LanguageStringUtils.setTextForSearch(company.getName()));
        doc.setShortName(company.getShortName());
        doc.setId(company.getId());
        doc.setCompanyType(company.getType() != null ? company.getType().getCode() : "");
        doc.setCountryCode(company.getCountry() != null ? company.getCountry().getIso() : "");
        doc.setCountryName(company.getCountry() != null ? company.getCountry().getCountryName() : "");
        doc.setTaxOffice(company.getTaxOffice() != null ? company.getTaxOffice().getName() : "");
        doc.setTaxOfficeCode(company.getTaxOfficeCode() != null ? company.getTaxOfficeCode() : "");
        doc.setTaxId(company.getTaxId() != null ? company.getTaxId() : "");
        doc.setDeletable(company.getMappedIds().isEmpty());
        doc.setLogoUrl(company.getLogoUrl());
        doc.setLogoFilePath(company.getLogoFilePath());
        doc.setWebsite(company.getWebsite());
        doc.setShortNameChecked(company.isShortNameChecked());
        doc.setStatsCustomer(company.getStatsCustomer());
        doc.setStatsParticipant(company.getStatsParticipant());
        company.getCompanyLocations().forEach(location ->
            doc.getLocations().add(CompanyLocationSearchDoc.fromCompanyLocation(location)));

        return doc;
    }
}
