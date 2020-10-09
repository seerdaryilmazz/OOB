package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.transportOrder.common.domain.IdNamePair;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private PostalAddress postaladdress;

    private String timezone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PostalAddress getPostaladdress() {
        return postaladdress;
    }

    public void setPostaladdress(PostalAddress postaladdress) {
        this.postaladdress = postaladdress;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public IdNamePair asIdNamePair(){
        return IdNamePair.createWith(getId(), getName());
    }
}
