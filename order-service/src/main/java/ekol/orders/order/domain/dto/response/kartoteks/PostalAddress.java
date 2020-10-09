package ekol.orders.order.domain.dto.response.kartoteks;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostalAddress {

    private String postalCode;
    private Country country;
    private String formattedAddress;
    private Point pointOnMap;

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public Point getPointOnMap() {
        return pointOnMap;
    }

    public void setPointOnMap(Point pointOnMap) {
        this.pointOnMap = pointOnMap;
    }
}
