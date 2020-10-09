package ekol.location.domain.location.comnon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.location.domain.Country;

import javax.persistence.*;

/**
 * Created by burak on 26/04/17.
 */
@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="country_id")
    private Country country;

    @Column(length = 100)
    private String region;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String district;

    @Column(nullable = false, length = 10)
    private String postalCode;

    @Column(nullable = false)
    private String streetAddress;

    @Column
    private String formattedAddress;


    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
}
