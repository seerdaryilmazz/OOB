package ekol.orders.order.domain.dto.response.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionResponse {

    private Long id;
    private String name;
    private CodeNamePair category;
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

    public CodeNamePair getCategory() {
        return category;
    }

    public void setCategory(CodeNamePair category) {
        this.category = category;
    }

    public IdNamePair getOperationRegion() {
        return operationRegion;
    }

    public void setOperationRegion(IdNamePair operationRegion) {
        this.operationRegion = operationRegion;
    }
}

