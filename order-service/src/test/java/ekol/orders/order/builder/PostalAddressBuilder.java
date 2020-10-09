package ekol.orders.order.builder;

import ekol.orders.order.domain.dto.response.kartoteks.Country;
import ekol.orders.order.domain.dto.response.kartoteks.Point;
import ekol.orders.order.domain.dto.response.kartoteks.PostalAddress;

public final class PostalAddressBuilder {

    private String postalCode;
    private Country country;
    private String formattedAddress;
    private Point pointOnMap;

    private PostalAddressBuilder() {
    }

    public static PostalAddressBuilder aPostalAddress() {
        return new PostalAddressBuilder();
    }

    public PostalAddressBuilder withPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public PostalAddressBuilder withCountry(Country country) {
        this.country = country;
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
        return aPostalAddress().withPostalCode(postalCode).withCountry(country)
                .withFormattedAddress(formattedAddress).withPointOnMap(pointOnMap);
    }

    public PostalAddress build() {
        PostalAddress postalAddress = new PostalAddress();
        postalAddress.setPostalCode(postalCode);
        postalAddress.setCountry(country);
        postalAddress.setFormattedAddress(formattedAddress);
        postalAddress.setPointOnMap(pointOnMap);
        return postalAddress;
    }
}
