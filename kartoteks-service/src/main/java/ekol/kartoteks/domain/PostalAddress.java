package ekol.kartoteks.domain;


import ekol.kartoteks.domain.common.Country;
import ekol.utils.StringComparison;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Locale;

/**
 * Created by fatmaozyildirim on 3/14/16.
 */
@Embeddable
@Access(AccessType.FIELD)
public class PostalAddress implements Serializable {

    private static final long serialVersionUID = 2L;
    @Column(nullable = false)
    private String streetName;

    @Column(length = 10)
    private String streetNumber;

    @Column(length = 10)
    private String doorNumber;

    @Column(nullable = false, length = 10)
    private String postalCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="country_id")
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Country country;

    @Column(length = 100)
    private String region;

    @Column(length = 100)
    private String city; //administrative_area_level_1

    @Column(length = 100)
    private String district; //administrative_area_level_2

    @Column(length = 100)
    private String locality; //administrative_area_level_3

    @Column(length = 100)
    private String suburb; //administrative_area_level_4

    @Column(length = 255)
    private String formattedAddress;

    @Embedded
    private Point pointOnMap;

    public PostalAddress() {
        //Default Constructor
    }

    public PostalAddress(String streetName, String postalCode, String region, Country country) {
        this.streetName = streetName;
        this.postalCode = postalCode;
        this.region = region;
        this.country = country;
    }
    public void toUpperCase(){
        Locale addressLocale = getCountry().getLocale();
        setStreetName(StringUtils.upperCase(getStreetName(), addressLocale));
        setPostalCode(StringUtils.upperCase(getPostalCode(), addressLocale));
        setRegion(StringUtils.upperCase(getRegion(), addressLocale));
        setCity(StringUtils.upperCase(getCity(), addressLocale));
        setDistrict(StringUtils.upperCase(getDistrict(), addressLocale));
        setLocality(StringUtils.upperCase(getLocality(), addressLocale));
        setSuburb(StringUtils.upperCase(getSuburb(), addressLocale));
    }

    public void copyFrom(PostalAddress addressData){
        if(addressData.getStreetName() != null){
            setStreetName(addressData.getStreetName());
        }
        if(addressData.getPostalCode() != null){
            setPostalCode(addressData.getPostalCode());
        }
        if(addressData.getCountry() != null){
            setCountry(addressData.getCountry());
        }
        if(addressData.getRegion() != null){
            setRegion(addressData.getRegion());
        }
        if(addressData.getCity() != null){
            setCity(addressData.getCity());
        }
        if(addressData.getDistrict() != null){
            setDistrict(addressData.getDistrict());
        }
        if(addressData.getLocality() != null){
            setLocality(addressData.getLocality());
        }
        if(addressData.getSuburb() != null){
            setSuburb(addressData.getSuburb());
        }
        if(addressData.getFormattedAddress() != null){
            setFormattedAddress(addressData.getFormattedAddress());
        }
    }

    public void generateFormattedAddress(){
        StringBuilder generatedFormattedAddress = new StringBuilder(64);
        generatedFormattedAddress.append(getStreetName());
        if(generatedFormattedAddress.length() > 0){
            generatedFormattedAddress.append(", ");
        }
        generatedFormattedAddress.append(getPostalCode());
        if(generatedFormattedAddress.length() > 0){
            generatedFormattedAddress.append(" ");
        }
        generatedFormattedAddress.append(getDistrict());
        if(generatedFormattedAddress.length() > 0){
            generatedFormattedAddress.append(", ");
        }
        generatedFormattedAddress.append(getCity());
        if(generatedFormattedAddress.length() > 0){
            generatedFormattedAddress.append(" ");
        }
        generatedFormattedAddress.append(getCountry().getCountryName());

        setFormattedAddress(generatedFormattedAddress.toString());
    }

    public boolean hasUpdatesToExport(PostalAddress other){
        boolean addressChanged = !StringComparison.equalsIgnoreCase(getStreetName(), other.getStreetName()) ||
                !StringComparison.equalsIgnoreCase(getPostalCode(), other.getPostalCode());
        boolean districtChanged = !StringComparison.equalsIgnoreCase(getCity(), other.getCity()) ||
                !StringComparison.equalsIgnoreCase(getDistrict(), other.getDistrict()) ||
                !StringComparison.equalsIgnoreCase(getRegion(), other.getRegion());
        return  addressChanged || districtChanged ||
                (getPointOnMap() != null && (!getPointOnMap().equals(other.getPointOnMap())));

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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Point getPointOnMap() {
        return pointOnMap;
    }

    public void setPointOnMap(Point pointOnMap) {
        this.pointOnMap = pointOnMap;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(String doorNumber) {
        this.doorNumber = doorNumber;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }
}
