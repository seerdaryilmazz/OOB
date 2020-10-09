package ekol.kartoteks.domain.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ekol.exceptions.ApplicationException;
import ekol.kartoteks.domain.Company;
import ekol.kartoteks.domain.RemoteApplication;
import ekol.kartoteks.domain.dto.UserJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by kilimci on 05/05/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyExchangeData {

    private Long kartoteksId;
    private String companyId;
    private String salesforceId;
    private String name;
    private List<LocationExchangeData> locations = new ArrayList<>();
    private String countryCode;
    private String website;
    private String emailDomain;
    private List<ContactExchangeData> contacts = new ArrayList<>();
    private List<SectorExchangeData> sectors = new ArrayList<>();
    private String defaultSubSectorCode;
    private String type;
    private String taxofficeCode;
    private String taxId;
    private String tckn;
    private String logoUrl;
    private String eoriNumber;
    private OrderExchangeData order;
    private boolean ownedByEkol;
    private List<RoleExchangeData> roles = new ArrayList<>();
    private List<RelationExchangeData> relations = new ArrayList<>();
    private UserJson accountOwner;

    public static CompanyExchangeData fromCompany(Company company){
        CompanyExchangeData exchangeData = new CompanyExchangeData();
        exchangeData.setKartoteksId(company.getId());
        exchangeData.setName(company.getName());
        exchangeData.setCountryCode(company.getCountry().getIso());
        exchangeData.setWebsite(company.getWebsite());
        exchangeData.setEmailDomain(company.getEmailDomain());
        exchangeData.setType(company.getType() != null ? company.getType().getCode() : null);
        exchangeData.setTaxofficeCode(company.getTaxOfficeCode());
        exchangeData.setTaxId(company.getTaxId());
        exchangeData.setTckn(company.getTckn());
        exchangeData.setLogoUrl(company.getLogoUrl());
        exchangeData.setOwnedByEkol(company.isOwnedByEkol());
        exchangeData.setEoriNumber(company.getEoriNumber());
        company.getMappedIds().stream().filter(mappedId -> mappedId.getApplication().equals(RemoteApplication.SALESFORCE))
                .forEach(mappedId -> exchangeData.setSalesforceId(mappedId.getApplicationCompanyId()));
        company.getCompanyLocations().forEach(companyLocation ->
            exchangeData.getLocations().add(LocationExchangeData.fromLocation(companyLocation))
        );
        company.getCompanyContacts().forEach(companyContact ->
                exchangeData.getContacts().add(ContactExchangeData.fromContact(companyContact))
        );
        company.getSectors().forEach(sector -> {
            SectorExchangeData sectorData = new SectorExchangeData();
            sectorData.setCode(sector.getSector().getCode());
            sectorData.setDefault(sector.isDefault());
            if(sector.isDefault()){
                exchangeData.setDefaultSubSectorCode(sector.getSector().getCode());
            }
            exchangeData.getSectors().add(sectorData);
        });
        company.getRoles().forEach(role ->
                exchangeData.getRoles().add(RoleExchangeData.fromCompanyRole(role))
        );
        company.getActiveRelations().forEach(companyRelation ->
                exchangeData.getRelations().add(
                        RelationExchangeData.fromCompanyRelation(companyRelation.getRelationType(), companyRelation.getPassiveCompany().getId()))
        );

        return exchangeData;
    }

    public String toJSON(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this) ;
        } catch (JsonProcessingException e) {
            throw new ApplicationException("JSON writing error", e);
        }
    }

    public Long getKartoteksId() {
        return kartoteksId;
    }

    public void setKartoteksId(Long kartoteksId) {
        this.kartoteksId = kartoteksId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getSalesforceId() {
        return salesforceId;
    }

    public void setSalesforceId(String salesforceId) {
        this.salesforceId = salesforceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LocationExchangeData> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationExchangeData> locations) {
        this.locations = locations;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmailDomain() {
        return emailDomain;
    }

    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
    }

    public List<ContactExchangeData> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactExchangeData> contacts) {
        this.contacts = contacts;
    }

    public List<SectorExchangeData> getSectors() {
        return sectors;
    }

    public void setSectors(List<SectorExchangeData> sectors) {
        this.sectors = sectors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTaxofficeCode() {
        return taxofficeCode;
    }

    public void setTaxofficeCode(String taxofficeCode) {
        this.taxofficeCode = taxofficeCode;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getTckn() {
        return tckn;
    }

    public void setTckn(String tckn) {
        this.tckn = tckn;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public List<RoleExchangeData> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleExchangeData> roles) {
        this.roles = roles;
    }

    public List<RelationExchangeData> getRelations() {
        return relations;
    }

    public void setRelations(List<RelationExchangeData> relations) {
        this.relations = relations;
    }

    public String getEoriNumber() {
        return eoriNumber;
    }

    public void setEoriNumber(String eoriNumber) {
        this.eoriNumber = eoriNumber;
    }

    public boolean isOwnedByEkol() {
        return ownedByEkol;
    }

    public void setOwnedByEkol(boolean ownedByEkol) {
        this.ownedByEkol = ownedByEkol;
    }

    public OrderExchangeData getOrder() {
        return order;
    }

    public void setOrder(OrderExchangeData order) {
        this.order = order;
    }

    public String getDefaultSubSectorCode() {
        return defaultSubSectorCode;
    }

    public void setDefaultSubSectorCode(String defaultSubSectorCode) {
        this.defaultSubSectorCode = defaultSubSectorCode;
    }

    public UserJson getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(UserJson accountOwner) {
        this.accountOwner = accountOwner;
    }
}
