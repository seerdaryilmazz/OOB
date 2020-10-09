package ekol.kartoteks.domain.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.kartoteks.domain.CompanyLocation;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kilimci on 05/05/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationExchangeData {

    private Long kartoteksId;
    private String locationId;
    private String salesforceId;
    private String name;
    private String streetName;
    private String postalCode;
    private String countryCode;
    private String region;
    private String city;
    private String district;
    private String locality;
    private String suburb;
    private String customsCode;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private boolean isActive;
    private boolean isDefault;
    private List<String> types = new ArrayList<>();
    private List<PhoneNumberExchangeData> phoneNumbers = new ArrayList<>();

    public static LocationExchangeData fromLocation(CompanyLocation location){
        LocationExchangeData exchangeData = new LocationExchangeData();
        exchangeData.setKartoteksId(location.getId());
        exchangeData.setName(location.getName());
        exchangeData.setStreetName(location.getPostaladdress().getStreetName() +
                (StringUtils.isNotBlank(location.getPostaladdress().getStreetNumber()) ? (" " + location.getPostaladdress().getStreetNumber()) : ""));
        exchangeData.setPostalCode(location.getPostaladdress().getPostalCode());
        exchangeData.setCountryCode(location.getPostaladdress().getCountry().getIso());
        exchangeData.setRegion(location.getPostaladdress().getRegion());
        exchangeData.setCity(location.getPostaladdress().getCity());
        exchangeData.setDistrict(location.getPostaladdress().getDistrict());
        exchangeData.setLocality(location.getPostaladdress().getLocality());
        exchangeData.setSuburb(location.getPostaladdress().getSuburb());
        exchangeData.setCustomsCode(location.getCustomsCode());
        if(location.getPostaladdress().getPointOnMap() != null){
            exchangeData.setLatitude(location.getPostaladdress().getPointOnMap().getLat().setScale(10,RoundingMode.FLOOR));
            exchangeData.setLongitude(location.getPostaladdress().getPointOnMap().getLng().setScale(10,RoundingMode.FLOOR));
        }
        exchangeData.setActive(location.isActive());
        exchangeData.setDefault(location.isDefault());
        location.getLocationTypes().forEach(locationType -> exchangeData.getTypes().add(locationType.getCode()));
        location.getPhoneNumbers().forEach(phoneNumberWithType ->
                exchangeData.getPhoneNumbers().add(PhoneNumberExchangeData.fromPhoneNumber(phoneNumberWithType))
        );
        return exchangeData;
    }

    public Long getKartoteksId() {
        return kartoteksId;
    }

    public void setKartoteksId(Long kartoteksId) {
        this.kartoteksId = kartoteksId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<PhoneNumberExchangeData> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumberExchangeData> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getSalesforceId() {
        return salesforceId;
    }

    public void setSalesforceId(String salesforceId) {
        this.salesforceId = salesforceId;
    }

    public String getCustomsCode() {
        return customsCode;
    }

    public void setCustomsCode(String customsCode) {
        this.customsCode = customsCode;
    }
}
