package ekol.location.domain.location.customs;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ekol.hibernate5.domain.entity.BaseEntity;
import ekol.location.domain.Country;
import ekol.location.domain.location.comnon.*;
import ekol.location.domain.location.comnon.ExternalSystemId.CollectionDeserializer;
import ekol.location.domain.location.comnon.ExternalSystemId.EntityNameValue;

@Entity
@Where(clause = "deleted = 0")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomsOfficeLocation extends BaseEntity{

    @Id
    @SequenceGenerator(name = "seq_customs_office_location", sequenceName = "seq_customs_office_location")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_customs_office_location")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customsOfficeId")
    @JsonBackReference("locations")
    private CustomsOffice customsOffice;

    @Column
    private String name;

    @Column
    private String localName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "countryId")
    private Country country;

    @Column
    private String address;

    @Column(length = 20)
    private String postalCode;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean office;

    @Column
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="customs_office_location_phone",
            joinColumns=@JoinColumn(name = "customsOfficeLocationId"))
    private Set<PhoneNumberWithType> phoneNumbers = new HashSet<>();

    @Column
    private String googlePlaceId;

    @Column
    private String googlePlaceUrl;

    @Embedded
    private Point pointOnMap;

    @Column
    private String timezone;

    @Embedded
    @ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "ExternalSystemId", joinColumns = {@JoinColumn(name = "parentId")})
    @AttributeOverrides({
    	@AttributeOverride(name="entityName", column=@Column(name="ENTITY_NAME")),
    	@AttributeOverride(name="externalSystem", column=@Column(name="EXTERNAL_SYSTEM")),
    	@AttributeOverride(name="externalId", column=@Column(name="EXTERNAL_ID"))
    })
    @Where(clause="ENTITY_NAME = 'CustomsOfficeLocation'")
    @EntityNameValue("CustomsOfficeLocation")
    @JsonDeserialize(using=CollectionDeserializer.class)
    private Collection<ExternalSystemId> externalIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomsOffice getCustomsOffice() {
        return customsOffice;
    }

    public void setCustomsOffice(CustomsOffice customsOffice) {
        this.customsOffice = customsOffice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isOffice() {
        return office;
    }

    public void setOffice(boolean office) {
        this.office = office;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<PhoneNumberWithType> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Set<PhoneNumberWithType> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
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

    public Point getPointOnMap() {
        return pointOnMap;
    }

    public void setPointOnMap(Point pointOnMap) {
        this.pointOnMap = pointOnMap;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

	public Collection<ExternalSystemId> getExternalIds() {
		return externalIds;
	}

	public void setExternalIds(Collection<ExternalSystemId> externalIds) {
		this.externalIds = externalIds;
	}
    
}
