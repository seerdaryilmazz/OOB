package ekol.kartoteks.builder;

import ekol.kartoteks.domain.*;
import ekol.kartoteks.domain.common.Country;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kilimci on 17/10/16.
 */
public final class CompanyBuilder {
    private Long id;
    private String name;
    private String localName;
    private Set<CompanyLocation> companyLocations = new HashSet<>();
    private Country country;
    private String website;
    private String emailDomain;
    private Set<CompanyContact> companyContacts = new HashSet<>() ;
    private Set<CompanySector> sectors = new HashSet<>() ;
    private CompanyType type;
    private TaxOffice taxoffice;
    private String taxid;
    private String tckn;
    private String logoUrl;
    private Set<CompanyRole> roles = new HashSet<>() ;
    private Set<CompanyRelation> activeRelations = new HashSet<>() ;
    private Set<CompanyRelation> passiveRelations = new HashSet<>() ;
    private Set<CompanyIdMapping> mappedIds = new HashSet<>() ;
    private Long code;
    private CompanySegmentType segmentType;
    private boolean deleted;
    private boolean ownedByEkol;
    private boolean shortNameChecked;

    private CompanyBuilder() {
    }

    public static CompanyBuilder aCompany() {
        return new CompanyBuilder();
    }

    public CompanyBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public CompanyBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CompanyBuilder withLocalName(String localName) {
        this.localName = localName;
        return this;
    }

    public CompanyBuilder withCompanyLocations(Set<CompanyLocation> companyLocations) {
        this.companyLocations = companyLocations;
        return this;
    }

    public CompanyBuilder withCountry(Country country) {
        this.country = country;
        return this;
    }

    public CompanyBuilder withWebsite(String website) {
        this.website = website;
        return this;
    }

    public CompanyBuilder withEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
        return this;
    }

    public CompanyBuilder withCompanyContacts(Set<CompanyContact> companyContacts) {
        this.companyContacts = companyContacts;
        return this;
    }

    public CompanyBuilder withSectors(Set<CompanySector> sectors) {
        this.sectors = sectors;
        return this;
    }

    public CompanyBuilder withType(CompanyType type) {
        this.type = type;
        return this;
    }

    public CompanyBuilder withTaxoffice(TaxOffice taxoffice) {
        this.taxoffice = taxoffice;
        return this;
    }

    public CompanyBuilder withTaxid(String taxid) {
        this.taxid = taxid;
        return this;
    }

    public CompanyBuilder withTckn(String tckn) {
        this.tckn = tckn;
        return this;
    }

    public CompanyBuilder withLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public CompanyBuilder withRoles(Set<CompanyRole> roles) {
        this.roles = roles;
        return this;
    }

    public CompanyBuilder withActiveRelations(Set<CompanyRelation> activeRelations) {
        this.activeRelations = activeRelations;
        return this;
    }

    public CompanyBuilder withPassiveRelations(Set<CompanyRelation> passiveRelations) {
        this.passiveRelations = passiveRelations;
        return this;
    }

    public CompanyBuilder withMappedIds(Set<CompanyIdMapping> mappedIds) {
        this.mappedIds = mappedIds;
        return this;
    }

    public CompanyBuilder withCode(Long code) {
        this.code = code;
        return this;
    }

    public CompanyBuilder withSegmentType(CompanySegmentType segmentType) {
        this.segmentType = segmentType;
        return this;
    }

    public CompanyBuilder withDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }
    public CompanyBuilder withOwnedByEkol(boolean ownedByEkol) {
        this.ownedByEkol = ownedByEkol;
        return this;
    }
    public CompanyBuilder withShortNameChecked(boolean shortNameChecked) {
        this.shortNameChecked = shortNameChecked;
        return this;
    }

    public CompanyBuilder but() {
        return aCompany().withId(id).withName(name).withLocalName(localName).withCompanyLocations(companyLocations).withCountry(country).withWebsite(website).withEmailDomain(emailDomain).withCompanyContacts(companyContacts).withSectors(sectors).withType(type).withTaxoffice(taxoffice).withTaxid(taxid).withTckn(tckn).withLogoUrl(logoUrl).withRoles(roles).withActiveRelations(activeRelations).withPassiveRelations(passiveRelations).withMappedIds(mappedIds).withCode(code).withSegmentType(segmentType).withDeleted(deleted);
    }

    public Company build() {
        Company company = new Company();
        company.setId(id);
        company.setName(name);
        company.setLocalName(localName);
        company.setCompanyLocations(companyLocations);
        company.setCountry(country);
        company.setWebsite(website);
        company.setEmailDomain(emailDomain);
        company.setCompanyContacts(companyContacts);
        company.setSectors(sectors);
        company.setType(type);
        company.setTaxOffice(taxoffice);
        company.setTaxId(taxid);
        company.setTckn(tckn);
        company.setLogoUrl(logoUrl);
        company.setRoles(roles);
        company.setActiveRelations(activeRelations);
        company.setPassiveRelations(passiveRelations);
        company.setMappedIds(mappedIds);
        company.setCode(code);
        company.setSegmentType(segmentType);
        company.setDeleted(deleted);
        company.setOwnedByEkol(ownedByEkol);
        company.setShortNameChecked(shortNameChecked);
        return company;
    }
}
