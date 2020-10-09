package ekol.orders.transportOrder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.transportOrder.common.domain.IdNamePair;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String firstName;
    private String lastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullname() {
        return getFirstName() + " " + getLastName();
    }

    public IdNamePair asIdNamePair(){
        return IdNamePair.createWith(getId(), getFullname());
    }
}
