package ekol.kartoteks.domain;

import java.util.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.*;
import org.hibernate.envers.*;

import com.fasterxml.jackson.annotation.*;

import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.kartoteks.domain.common.Country;
import ekol.utils.StringComparison;

/**
 * Created by fatmaozyildirim on 3/14/16.
 */
@Entity
@NamedEntityGraphs({
        @NamedEntityGraph(name = "Company.allDetails",
                attributeNodes = {@NamedAttributeNode("companyLocations"),
                        @NamedAttributeNode("companyContacts"),
                        @NamedAttributeNode("taxOffice"),
                        @NamedAttributeNode("country"),
                        @NamedAttributeNode("sectors"),
                        @NamedAttributeNode("roles"),
                        @NamedAttributeNode("activeRelations"),
                        @NamedAttributeNode("passiveRelations"),
                        @NamedAttributeNode("segmentType"),
                        @NamedAttributeNode("mappedIds")}),

        @NamedEntityGraph(name = "Company.relations",
                attributeNodes = {
                        @NamedAttributeNode("activeRelations"),
                        @NamedAttributeNode("passiveRelations"),
                        @NamedAttributeNode("segmentType")})
})
@Table(name = "Company")
@SequenceGenerator(name = "SEQ_COMPANY", sequenceName = "SEQ_COMPANY")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
@Where(clause = "deleted = 0")
@Audited
public class Company extends BaseEntity implements IdNameSerializable{

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_COMPANY")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String localName;

    @Column(length = 30)
    private String shortName;

    @OneToMany(mappedBy="company", fetch = FetchType.LAZY)
    @Where(clause="deleted=0")
    private Set<CompanyLocation> companyLocations = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id")
    @NotAudited
    private Country country;

    @Column(length = 100)
    private String website;

    @Column(length = 100)
    private String emailDomain;

    @OneToMany(mappedBy="company", fetch = FetchType.LAZY)
    @Where(clause="deleted=0")
    @JsonManagedReference
    private Set<CompanyContact> companyContacts = new HashSet<>() ;

    @OneToMany(mappedBy="company", fetch = FetchType.LAZY)
    @Where(clause="deleted=0")
    @JsonManagedReference
    private Set<CompanySector> sectors = new HashSet<>() ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_type_id")
    @NotAudited
    private CompanyType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_office_id")
    @NotAudited
    private TaxOffice taxOffice;

    @Column(length = 20)
    private String taxOfficeCode;

    @Column(length = 20)
    private String taxId;

    @Column (length = 20)
    private String tckn;

    @Column
    private String logoUrl;

    @Column
    private String logoFilePath;

    @OneToMany(mappedBy="company", fetch = FetchType.LAZY)
    @Where(clause="deleted=0")
    @JsonManagedReference
    private Set<CompanyRole> roles = new HashSet<>() ;

    @OneToMany(mappedBy="activeCompany", fetch = FetchType.LAZY)
    @Where(clause="deleted=0")
    private Set<CompanyRelation> activeRelations = new HashSet<>() ;

    @OneToMany(mappedBy="passiveCompany", fetch = FetchType.LAZY)
    @Where(clause="deleted=0")
    private Set<CompanyRelation> passiveRelations = new HashSet<>() ;

    @OneToMany(mappedBy="company", fetch = FetchType.LAZY)
    @Where(clause="deleted=0")
    @JsonManagedReference
    @NotAudited
    private Set<CompanyIdMapping> mappedIds = new HashSet<>() ;

    @Column
    private Long code;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_segment_type_id")
    @NotAudited
    private CompanySegmentType segmentType;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean ownedByEkol;

    @Column(length = 50)
    private String eoriNumber;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean shortNameChecked = false;

    private Integer statsCustomer = 0;
    private Integer statsParticipant = 0;

    @Transient
    private Long mergeWithCompanyId;
    
    public boolean isTemp() {
    	return getCompanyLocations().stream().allMatch(CompanyLocation::isTemp);
    }

    public void toUpperCase(){
        setName(nameToUpperCase());
        setLocalName(localNameToUpperCase());
        setShortName(shortNameToUpperCase());
        getCompanyLocations().forEach(CompanyLocation::toUpperCase);
        getCompanyContacts().forEach(CompanyContact::toUpperCase);
    }

    public String nameToUpperCase(){
        return StringUtils.upperCase(getName(), getCountry().getLocale());
    }
    public String localNameToUpperCase(){
        return StringUtils.upperCase(getLocalName(), getCountry().getLocale());
    }
    public String shortNameToUpperCase(){
        return StringUtils.upperCase(getShortName(), getCountry().getLocale());
    }

    private void copyNames(Company data){
        if(data.getName() != null) {
            setName(data.getName());
        }
        if(data.getLocalName() != null) {
            setLocalName(data.getLocalName());
        }
    }
    private void copyTaxInfo(Company data){
        if(data.getType() != null){
            setType(data.getType());
        }
        if(data.getTckn() != null){
            setTckn(data.getTckn());
        }
        if(data.getTaxOffice() != null){
            setTaxOffice(data.getTaxOffice());
        }
        if(data.getTaxOfficeCode() != null){
            setTaxOfficeCode(data.getTaxOfficeCode());
        }
        if(data.getTaxId() != null){
            setTaxId(data.getTaxId());
        }
    }
    public void copyFrom(Company data){
        copyNames(data);
        if(data.getCountry() != null){
            setCountry(data.getCountry());
        }
        if(data.getWebsite() != null){
            setWebsite(data.getWebsite());
        }
        if(data.getEmailDomain() != null){
            setEmailDomain(data.getEmailDomain());
        }
        if(data.getLogoUrl() != null){
            setLogoUrl(data.getLogoUrl());
        }
        setOwnedByEkol(data.isOwnedByEkol());
        setEoriNumber(data.getEoriNumber());
        copyTaxInfo(data);
    }

    public boolean hasUpdatesToExport(Company other){
        // TODO: Şuan için sektör bilgilerinin değişikliği export işleminin oluşmasını etkilemiyor. Eğer etkileyecekse CompanySaveService.updateSectors metodu da gözden geçirilmeli.
    	
    	boolean tempChanged = isTemp() != other.isTemp();
        boolean companyChanged = !StringComparison.equalsIgnoreCase(getName(), other.getName()) ||
                ObjectUtils.notEqual(getCountry(), other.getCountry()) ||
                ObjectUtils.notEqual(getType(), other.getType());
        boolean websiteChanged = !StringComparison.equalsIgnoreCase(getWebsite(), other.getWebsite()) ||
                !StringComparison.equalsIgnoreCase(getEmailDomain(), other.getEmailDomain());
        boolean taxChanged = ObjectUtils.notEqual(getTaxOffice(), other.getTaxOffice()) ||
                !StringComparison.equalsIgnoreCase(getTaxId(), other.getTaxId()) ||
                !StringComparison.equalsIgnoreCase(getTckn(), other.getTckn());
        boolean eoriChanged = !StringComparison.equalsIgnoreCase(getEoriNumber(), other.getEoriNumber());
        boolean sectorChanged = ekol.kartoteks.utils.ObjectUtils.areDifferent(getSectors(),other.getSectors(), ekol.kartoteks.utils.ObjectUtils.COMPANY_SECTOR_COMPARATOR);

        return tempChanged || (companyChanged || websiteChanged) || (taxChanged || eoriChanged) || sectorChanged;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CompanyLocation> getCompanyLocations() {
        return companyLocations;
    }

    public void setCompanyLocations(Set<CompanyLocation> companyLocations) {
        this.companyLocations = companyLocations;
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

    public Set<CompanyContact> getCompanyContacts() {
        return companyContacts;
    }

    public void setCompanyContacts(Set<CompanyContact> companyContacts) {
        this.companyContacts = companyContacts;
    }

    public Set<CompanySector> getSectors() {
        return sectors;
    }

    public void setSectors(Set<CompanySector> sectors) {
        this.sectors = sectors;
    }

    public CompanyType getType() {
        return type;
    }

    public void setType(CompanyType type) {
        this.type = type;
    }

    public TaxOffice getTaxOffice() {
        return taxOffice;
    }

    public void setTaxOffice(TaxOffice taxOffice) {
        this.taxOffice = taxOffice;
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

    public String getLogoFilePath() {
        return logoFilePath;
    }

    public void setLogoFilePath(String logoFilePath) {
        this.logoFilePath = logoFilePath;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public Set<CompanyIdMapping> getMappedIds() {
        return mappedIds;
    }

    public void setMappedIds(Set<CompanyIdMapping> mappedIds) {
        this.mappedIds = mappedIds;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Set<CompanyRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<CompanyRole> roles) {
        this.roles = roles;
    }

    public Set<CompanyRelation> getActiveRelations() {
        return activeRelations;
    }

    public void setActiveRelations(Set<CompanyRelation> activeRelations) {
        this.activeRelations = activeRelations;
    }

    public Set<CompanyRelation> getPassiveRelations() {
        return passiveRelations;
    }

    public void setPassiveRelations(Set<CompanyRelation> passiveRelations) {
        this.passiveRelations = passiveRelations;
    }

    public CompanySegmentType getSegmentType() {
        return segmentType;
    }

    public void setSegmentType(CompanySegmentType segmentType) {
        this.segmentType = segmentType;
    }

    public boolean isOwnedByEkol() {
        return ownedByEkol;
    }

    public void setOwnedByEkol(boolean ownedByEkol) {
        this.ownedByEkol = ownedByEkol;
    }

    public String getEoriNumber() {
        return eoriNumber;
    }

    public void setEoriNumber(String eoriNumber) {
        this.eoriNumber = eoriNumber;
    }

    public String getTaxOfficeCode() {
        return taxOfficeCode;
    }

    public void setTaxOfficeCode(String taxOfficeCode) {
        this.taxOfficeCode = taxOfficeCode;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean isShortNameChecked() {
        return shortNameChecked;
    }

    public void setShortNameChecked(boolean shortNameChecked) {
        this.shortNameChecked = shortNameChecked;
    }

    public Integer getStatsCustomer() {
        return statsCustomer;
    }

    public void setStatsCustomer(Integer statsCustomer) {
        this.statsCustomer = statsCustomer;
    }

    public Integer getStatsParticipant() {
        return statsParticipant;
    }

    public void setStatsParticipant(Integer statsParticipant) {
        this.statsParticipant = statsParticipant;
    }

    public Long getMergeWithCompanyId() {
        return mergeWithCompanyId;
    }

    public void setMergeWithCompanyId(Long mergeWithCompanyId) {
        this.mergeWithCompanyId = mergeWithCompanyId;
    }
}
