package ekol.orders.transportOrder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.orders.transportOrder.common.domain.IdNamePair;
import ekol.orders.transportOrder.common.domain.IdNamePairStr;

/**
 * Created by burak on 27/09/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Region {

    private Long id;
    private String name;

    private IdNamePairStr category;

    private IdNamePair operationRegion;

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

    public IdNamePairStr getCategory() {
        return category;
    }

    public void setCategory(IdNamePairStr category) {
        this.category = category;
    }

    public IdNamePair getOperationRegion() {
        return operationRegion;
    }

    public void setOperationRegion(IdNamePair operationRegion) {
        this.operationRegion = operationRegion;
    }

}
