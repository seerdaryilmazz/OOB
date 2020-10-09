package ekol.kartoteks.domain;

import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.kartoteks.serializers.IdNameSerializer;
import ekol.utils.StringComparison;

/**
 * Created by fatmaozyildirim on 3/14/16.
 */
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "CompanyLocation.company",
                attributeNodes = {
                        @NamedAttributeNode("company")
                }
        )
})
@Entity
@Table(name = "CompanyLocation")
@SequenceGenerator(name = "SEQ_COMPANYLOCATION", sequenceName = "SEQ_COMPANYLOCATION")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"deleted", "deletedAt", "lastUpdated", "lastUpdatedBy"})
@Where(clause = "deleted = 0")
@Audited
public class CompanyLocation extends BaseEntity implements IdNameSerializable{
    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_COMPANYLOCATION")
    private Long id ;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 30)
    private String shortName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonSerialize(using = IdNameSerializer.class)
    private Company company;

    @Embedded
    private PostalAddress postaladdress;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="company_location_phone",
            joinColumns=@JoinColumn(name = "company_location_id"))
    private Set<PhoneNumberWithType> phoneNumbers = new HashSet<>();

    @Column(nullable=false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active = true;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "company_location_type",
            joinColumns=@JoinColumn(name = "company_location_id"))
    @NotAudited
    private List<LocationType> locationTypes = new ArrayList<>();


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "company_location_adr_comp",
            joinColumns=@JoinColumn(name = "company_location_id"))
    private Map<String, String> addressComponents = new HashMap<>();

    @OneToMany(mappedBy="companyLocation", fetch = FetchType.EAGER)
    @Where(clause="deleted=0")
    @JsonManagedReference(value = "mappedIds")
    @NotAudited
    private Set<CompanyLocationIdMapping> mappedIds = new HashSet<>();

    @Column(length = 100)
    private String customsCode;

    @Column(length = 100)
    private String timezone;

    @Column(length = 255)
    private String googlePlaceId;

    @Column(length = 255)
    private String googlePlaceUrl;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isDefault = false;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean shortNameChecked = false;

    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean pointOnMapConfirmed = false;
    
    @Transient
    @JsonInclude(value = Include.NON_DEFAULT)
    private boolean updated;

    public CompanyLocation(){
        //Default Constructor
    }
    
    @Transient
    public boolean isTemp() {
    	return !mappedIds.stream()
    			.filter(t->t.getApplication() == RemoteApplication.QUADRO)
    			.findFirst()
    			.map(CompanyLocationIdMapping::getApplicationLocationId)
    			.isPresent();
    }

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
        CompanyLocation location = (CompanyLocation) other;
        EqualsBuilder builder = new EqualsBuilder();
        if(location.getId() != null){
            builder.append(id, location.getId());
        }else{
            builder.append(name, location.getName());
        }
        return builder.isEquals();

    }
    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        if(getId() != null){
            builder.append(getId());
        }else{
            builder.append(getName());
        }
        return builder.toHashCode();
    }

    public void toUpperCase(){
        Locale addressLocale = getPostaladdress().getCountry().getLocale();
        setName(getName().toUpperCase(addressLocale));
        if(StringUtils.isNotBlank(getShortName())){
            setShortName(getShortName().toUpperCase(addressLocale));
        }
        getPostaladdress().toUpperCase();
    }

    public void copyFrom(CompanyLocation locationData){
        if(locationData.getName() != null){
            setName(locationData.getName());
        }

        if(locationData.getPostaladdress() != null){
            getPostaladdress().copyFrom(locationData.getPostaladdress());
        }
        if(locationData.getLocationTypes() != null){
            setLocationTypes(locationData.getLocationTypes());
        }
        setCustomsCode(locationData.getCustomsCode());
        setDefault(locationData.isDefault());
        setActive(locationData.isActive());
        setDeleted(locationData.isDeleted());
        mergePhoneNumbers(locationData.getPhoneNumbers());

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

    public boolean hasUpdatesToExport(CompanyLocation other){
    	return isTemp() != other.isTemp() ||
				!StringComparison.equalsIgnoreCase(getName(), other.getName()) ||
				getPostaladdress().hasUpdatesToExport(other.getPostaladdress()) ||
                !StringComparison.equalsIgnoreCase(getCustomsCode(), other.getCustomsCode());
    }
    
    public static CompanyLocation withName(String name){
        CompanyLocation location = new CompanyLocation();
        location.setName(name);
        return location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PostalAddress getPostaladdress() {
        return postaladdress;
    }

    public void setPostaladdress(PostalAddress postaladdress) {
        this.postaladdress = postaladdress;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Set<PhoneNumberWithType> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Set<PhoneNumberWithType> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<LocationType> getLocationTypes() {
        return locationTypes;
    }

    public void setLocationTypes(List<LocationType> locationTypes) {
        this.locationTypes = locationTypes;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Set<CompanyLocationIdMapping> getMappedIds() {
        return mappedIds;
    }

    public void setMappedIds(Set<CompanyLocationIdMapping> mappedIds) {
        this.mappedIds = mappedIds;
    }

    public Map<String, String> getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(Map<String, String> addressComponents) {
        this.addressComponents = addressComponents;
    }

    public String getCustomsCode() {
        return customsCode;
    }

    public void setCustomsCode(String customsCode) {
        this.customsCode = customsCode;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getGooglePlaceId() {
        return googlePlaceId;
    }

    public void setGooglePlaceId(String googlePlaceId) {
        this.googlePlaceId = googlePlaceId;
    }

    public String getGooglePlaceUrl() {
        return googlePlaceUrl;
    }

    public void setGooglePlaceUrl(String googlePlaceUrl) {
        this.googlePlaceUrl = googlePlaceUrl;
    }

    public boolean isShortNameChecked() {
        return shortNameChecked;
    }

    public void setShortNameChecked(boolean shortNameChecked) {
        this.shortNameChecked = shortNameChecked;
    }

    public boolean isPointOnMapConfirmed() {
        return pointOnMapConfirmed;
    }

    public void setPointOnMapConfirmed(boolean pointOnMapConfirmed) {
        this.pointOnMapConfirmed = pointOnMapConfirmed;
    }

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
}
