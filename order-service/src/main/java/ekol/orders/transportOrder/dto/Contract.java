package ekol.orders.transportOrder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.transportOrder.common.domain.IdNamePair;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contract implements Serializable {

    private Long id;

    private String code;

    private String name;

    private IdNamePair customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IdNamePair getCustomer() {
        return customer;
    }

    public void setCustomer(IdNamePair customer) {
        this.customer = customer;
    }
}
