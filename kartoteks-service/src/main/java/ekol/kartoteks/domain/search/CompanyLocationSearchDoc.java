package ekol.kartoteks.domain.search;


import ekol.kartoteks.domain.CompanyLocation;
import ekol.kartoteks.utils.LanguageStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created by kilimci on 26/03/16.
 */
public class CompanyLocationSearchDoc {

    @Field(type = FieldType.Long, index = FieldIndex.not_analyzed)
    private Long id;
    @Field(type = FieldType.String)
    private String name;
    @Field(type = FieldType.String)
    private String shortName;
    @Field(type = FieldType.String)
    private String streetName;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String postalCode;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String city;
    @Field(type = FieldType.String)
    private String cityAccentsStripped;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String district;
    @Field(type = FieldType.String)
    private String districtAccentsStripped;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String countryName;
    @Field(type = FieldType.String)
    private String countryCode;
    @Field(type = FieldType.Boolean)
    private boolean isDefault;
    @Field(type = FieldType.Boolean)
    private boolean active;
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String formattedAddress;
    @Field(type = FieldType.Boolean)
    private boolean shortNameChecked;

    public static CompanyLocationSearchDoc fromCompanyLocation(CompanyLocation location) {
        CompanyLocationSearchDoc doc = new CompanyLocationSearchDoc();
        doc.setId(location.getId());
        doc.setName(location.getName());
        doc.setShortName(location.getShortName());
        doc.setStreetName(location.getPostaladdress().getStreetName());
        doc.setPostalCode(location.getPostaladdress().getPostalCode());
        doc.setCity(location.getPostaladdress().getCity());
        if (StringUtils.isNotBlank(location.getPostaladdress().getCity())) {
            doc.setCityAccentsStripped(LanguageStringUtils.stripAccents(location.getPostaladdress().getCity()));
        }
        doc.setDistrict(location.getPostaladdress().getDistrict());
        if (StringUtils.isNotBlank(location.getPostaladdress().getDistrict())) {
            doc.setDistrictAccentsStripped(LanguageStringUtils.stripAccents(location.getPostaladdress().getDistrict()));
        }
        doc.setCountryName(location.getPostaladdress().getCountry().getCountryName());
        doc.setCountryCode(location.getPostaladdress().getCountry().getIso());
        doc.setFormattedAddress(location.getPostaladdress().getFormattedAddress());
        doc.setActive(location.isActive());
        doc.setShortNameChecked(location.isShortNameChecked());
        doc.setDefault(location.isDefault());
        return doc;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityAccentsStripped() {
        return cityAccentsStripped;
    }

    public void setCityAccentsStripped(String cityAccentsStripped) {
        this.cityAccentsStripped = cityAccentsStripped;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrictAccentsStripped() {
        return districtAccentsStripped;
    }

    public void setDistrictAccentsStripped(String districtAccentsStripped) {
        this.districtAccentsStripped = districtAccentsStripped;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public boolean isShortNameChecked() {
        return shortNameChecked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setShortNameChecked(boolean shortNameChecked) {
        this.shortNameChecked = shortNameChecked;
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
}
