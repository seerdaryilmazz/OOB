package ekol.orders.order.builder;

import ekol.orders.order.domain.dto.response.kartoteks.LocationResponse;
import ekol.orders.order.domain.dto.response.kartoteks.PostalAddress;

public final class LocationResponseBuilder {
    private Long id;
    private String name;
    private String timezone;
    private PostalAddress postaladdress;

    private LocationResponseBuilder() {
    }

    public static LocationResponseBuilder aLocationResponse() {
        return new LocationResponseBuilder();
    }

    public LocationResponseBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public LocationResponseBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public LocationResponseBuilder withTimezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    public LocationResponseBuilder withPostaladdress(PostalAddress postaladdress) {
        this.postaladdress = postaladdress;
        return this;
    }

    public LocationResponseBuilder but() {
        return aLocationResponse().withId(id).withName(name).withTimezone(timezone).withPostaladdress(postaladdress);
    }

    public LocationResponse build() {
        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setId(id);
        locationResponse.setName(name);
        locationResponse.setTimezone(timezone);
        locationResponse.setPostaladdress(postaladdress);
        return locationResponse;
    }
}