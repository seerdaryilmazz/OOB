package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostalAddress implements Serializable {

    private static final long serialVersionUID = 1L;

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
