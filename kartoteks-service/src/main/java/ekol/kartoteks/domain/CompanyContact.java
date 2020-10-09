package ekol.kartoteks.domain;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.*;
import org.hibernate.annotations.*;
import org.hibernate.envers.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.kartoteks.serializers.IdNameSerializer;
import ekol.kartoteks.utils.ObjectUtils;

@NamedEntityGraphs({
    @NamedEntityGraph(
            name = "CompanyContact.withCompany",
            attributeNodes = {
                    @NamedAttributeNode(value = "company")
            }
    )
})
@Entity
@Table(name = "CompanyContact")
@SequenceGenerator(name = "SEQ_COMPANYCONTACT", sequenceName = "SEQ_COMPANYCONTACT")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
@Where(clause = "deleted = 0")
@Audited
public class CompanyContact extends BaseEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_COMPANYCONTACT")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Column(length = 10)
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    @NotAudited
    private ContactDepartment department;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "title_id")
    @NotAudited
    private ContactTitle title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = IdNameSerializer.class)
    @JoinColumn(name = "company_location_id")
    private CompanyLocation companyLocation;

    @Column
    private String linkedinUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="company_contact_phone",
            joinColumns=@JoinColumn(name = "company_contact_id"))
    private Set<PhoneNumberWithType> phoneNumbers = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="company_contact_email",
            joinColumns=@JoinColumn(name = "company_contact_id"))
    @JoinColumn(name = "company_contact_id")
    private Set<EmailWithType> emails = new HashSet<>();

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active = true;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isDefault = false;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="company_contact_serv_type",
            joinColumns=@JoinColumn(name = "company_contact_id"))
    @NotAudited
    private List<BusinessSegmentType> companyServiceTypes = new ArrayList<>();

    @OneToMany(mappedBy="companyContact", fetch = FetchType.EAGER)
    @Where(clause="deleted=0")
    @JsonManagedReference
    @NotAudited
    private Set<CompanyContactIdMapping> mappedIds = new HashSet<>();

    @Override
    public boolean equals(Object other){
        if(other == null){
            return true;
        }
        if(this == other){
            return true;
        }
        if (getClass() != other.getClass()){
            return false;
        }
        CompanyContact contact = (CompanyContact) other;
        EqualsBuilder builder = new EqualsBuilder();
        if(contact.getId() != null){
            builder.append(getId(), contact.getId());
        }else{
            builder.append(getFirstName(), contact.getFirstName());
            builder.append(getLastName(), contact.getLastName());
            builder.append(getEmails(), contact.getEmails());
        }
        return builder.isEquals();

    }
    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        if(getId() != null){
            builder.append(getId());
        }else{
            builder.append(getFirstName());
            builder.append(getLastName());
            builder.append(getEmails());
            builder.append(isActive());
        }
        return builder.toHashCode();
    }


    public void toUpperCase(){
        Locale defaultLocale = getCompany().getCountry().getLocale();
        setFirstName(StringUtils.upperCase(getFirstName(), defaultLocale));
        setLastName(StringUtils.upperCase(getLastName(), defaultLocale));
    }

    public void copyFrom(CompanyContact contactData){
        if(contactData.getFirstName() != null){
            setFirstName(contactData.getFirstName());
        }
        if(contactData.getLastName() != null){
            setLastName(contactData.getLastName());
        }
        if(contactData.getGender() != null){
            setGender(contactData.getGender());
        }
        if(contactData.getDepartment() != null){
            setDepartment(contactData.getDepartment());
        }
        if(contactData.getTitle() != null){
            setTitle(contactData.getTitle());
        }
        if(contactData.getCompanyLocation() != null){
            setCompanyLocation(contactData.getCompanyLocation());
        }
        if(contactData.getCompanyServiceTypes() != null){
            setCompanyServiceTypes(contactData.getCompanyServiceTypes());
        }
        setActive(contactData.isActive());
        setDeleted(contactData.isDeleted());
        mergePhoneNumbers(contactData.getPhoneNumbers());
        mergeEmails(contactData.getEmails());

    }

    private void mergePhoneNumbers(Set<PhoneNumberWithType> phoneNumberData){
        if(phoneNumberData.isEmpty()){
            return;
        }
        List<PhoneNumberWithType> newNumbers = phoneNumberData.stream().filter(eachNew ->
                getPhoneNumbers().stream().noneMatch(eachExisting -> eachExisting.getPhoneNumber().equals(eachNew.getPhoneNumber()))
        ).collect(Collectors.toList());

        phoneNumberData.stream().forEach(eachNew ->
                getPhoneNumbers().stream().forEach(eachExisting -> eachExisting.copyTypesIfNumbersEqual(eachNew))
        );

        getPhoneNumbers().addAll(newNumbers);
    }

    private void mergeEmails(Set<EmailWithType> emailData){
        if(emailData.isEmpty()){
            return;
        }
        List<EmailWithType> newEmails = emailData.stream().filter(eachNew ->
                getEmails().stream().noneMatch(eachExisting -> eachExisting.getEmail().equals(eachNew.getEmail()))
        ).collect(Collectors.toList());

        emailData.stream().forEach(eachNew ->
                getEmails().stream().forEach(eachExisting -> eachExisting.copyTypesIfEmailsEqual(eachNew))
        );

        getEmails().addAll(newEmails);

    }

    public void deleteInvalidNumbers(){
        List<PhoneNumberWithType> invalidNumbers = getPhoneNumbers().stream().filter(phone ->
                org.apache.commons.lang.StringUtils.isBlank(phone.getPhoneCountryCode()) ||
                        org.apache.commons.lang.StringUtils.isBlank(phone.getPhoneNumberWithoutCodes())
        ).collect(Collectors.toList());
        getPhoneNumbers().removeAll(invalidNumbers);
    }

    public boolean hasUpdatesToExport(CompanyContact other) {
        return
                ObjectUtils.areDifferent(getFirstName(), other.getFirstName()) ||
                ObjectUtils.areDifferent(getLastName(), other.getLastName()) ||
                ObjectUtils.areDifferent(getGender(), other.getGender()) ||
                ObjectUtils.areDifferent(getDepartment(), other.getDepartment(), ObjectUtils.DEPARTMENT_COMPARATOR) ||
                ObjectUtils.areDifferent(getTitle(), other.getTitle(), ObjectUtils.TITLE_COMPARATOR) ||
                ObjectUtils.areDifferent(getLinkedinUrl(), other.getLinkedinUrl()) ||
                ObjectUtils.areDifferent(getPhoneNumbers(), other.getPhoneNumbers(), ObjectUtils.PHONE_NUMBERS_COMPARATOR) ||
                ObjectUtils.areDifferent(getEmails(), other.getEmails(), ObjectUtils.EMAILS_COMPARATOR) ||
                isActive() != other.isActive() ||
                isDefault() != other.isDefault() ||
                ObjectUtils.areDifferent(getCompanyServiceTypes(), other.getCompanyServiceTypes(), ObjectUtils.BUSINESS_SEGMENT_TYPES_COMPARATOR);
    }

    public String getFullname(){
        return getFirstName() + " " + getLastName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Set<PhoneNumberWithType> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Set<PhoneNumberWithType> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public Set<EmailWithType> getEmails() {
        return emails;
    }

    public void setEmails(Set<EmailWithType> emails) {
        this.emails = emails;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public List<BusinessSegmentType> getCompanyServiceTypes() {
        return companyServiceTypes;
    }

    public void setCompanyServiceTypes(List<BusinessSegmentType> companyServiceTypes) {
        this.companyServiceTypes = companyServiceTypes;
    }

    public ContactDepartment getDepartment() {
        return department;
    }

    public void setDepartment(ContactDepartment department) {
        this.department = department;
    }

    public ContactTitle getTitle() {
        return title;
    }

    public void setTitle(ContactTitle title) {
        this.title = title;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Set<CompanyContactIdMapping> getMappedIds() {
        return mappedIds;
    }

    public void setMappedIds(Set<CompanyContactIdMapping> mappedIds) {
        this.mappedIds = mappedIds;
    }

    public CompanyLocation getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(CompanyLocation companyLocation) {
        this.companyLocation = companyLocation;
    }
}

