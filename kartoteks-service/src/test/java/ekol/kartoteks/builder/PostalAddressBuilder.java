package ekol.kartoteks.builder;


import ekol.kartoteks.domain.Point;
import ekol.kartoteks.domain.PostalAddress;
import ekol.kartoteks.domain.common.Country;

/**
 * Created by kilimci on 14/10/16.
 */
public final class PostalAddressBuilder {
    private String streetName;
    private String streetNumber;
    private String doorNumber;
    private String postalCode;
    private Country country;
    private String region;
    private String city; //administrative_area_level_1
    private String district; //administrative_area_level_2
    private String locality; //administrative_area_level_3
    private String suburb; //administrative_area_level_4
    private String formattedAddress;
    private Point pointOnMap;

    private PostalAddressBuilder() {
    }

    public static PostalAddressBuilder aPostalAddress() {
        return new PostalAddressBuilder();
    }

    public PostalAddressBuilder withStreetName(String streetName) {
        this.streetName = streetName;
        return this;
    }

    public PostalAddressBuilder withStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
        return this;
    }

    public PostalAddressBuilder withDoorNumber(String doorNumber) {
        this.doorNumber = doorNumber;
        return this;
    }

    public PostalAddressBuilder withPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public PostalAddressBuilder withCountry(Country country) {
        this.country = country;
        return this;
    }

    public PostalAddressBuilder withRegion(String region) {
        this.region = region;
        return this;
    }

    public PostalAddressBuilder withCity(String city) {
        this.city = city;
        return this;
    }

    public PostalAddressBuilder withDistrict(String district) {
        this.district = district;
        return this;
    }

    public PostalAddressBuilder withLocality(String locality) {
        this.locality = locality;
        return this;
    }

    public PostalAddressBuilder withSuburb(String suburb) {
        this.suburb = suburb;
        return this;
    }

    public PostalAddressBuilder withFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
        return this;
    }

    public PostalAddressBuilder withPointOnMap(Point pointOnMap) {
        this.pointOnMap = pointOnMap;
        return this;
    }

    public PostalAddressBuilder but() {
        return aPostalAddress().withStreetName(streetName).withStreetNumber(streetNumber).withDoorNumber(doorNumber).withPostalCode(postalCode).withCountry(country).withRegion(region).withCity(city).withDistrict(district).withLocality(locality).withSuburb(suburb).withFormattedAddress(formattedAddress).withPointOnMap(pointOnMap);
    }

    public PostalAddress build() {
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setStreetName(streetName);
        postalAddress.setStreetNumber(streetNumber);
        postalAddress.setDoorNumber(doorNumber);
        postalAddress.setPostalCode(postalCode);
        postalAddress.setCountry(country);
        postalAddress.setRegion(region);
        postalAddress.setCity(city);
        postalAddress.setDistrict(district);
        postalAddress.setLocality(locality);
        postalAddress.setSuburb(suburb);
        postalAddress.setFormattedAddress(formattedAddress);
        postalAddress.setPointOnMap(pointOnMap);
        return postalAddress;
    }
}
