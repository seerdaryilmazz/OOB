package ekol.orders.transportOrder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.transportOrder.common.domain.IdNamePair;

import java.io.Serializable;

/**
 * Created by kilimci on 22/08/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Warehouse implements Serializable {

    private Long id;
    private String name;
    private IdNamePair companyLocation;

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

    public IdNamePair getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(IdNamePair companyLocation) {
        this.companyLocation = companyLocation;
    }
}
