package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String countryName;

    private String iso;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }
}
