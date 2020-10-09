package ekol.orders.transportOrder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by kilimci on 30/09/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerRepTeamTag{
    private Long id;
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}