package ekol.location.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.location.domain.CollectionRegion;
import ekol.location.domain.DistributionRegion;
import ekol.location.domain.OperationRegion;
import ekol.location.domain.RegionCategory;
import ekol.location.domain.location.comnon.IdNameEmbeddable;

/**
 * Created by burak on 27/09/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegionOfLocationResponse {

    private Long id;
    private String name;

    private RegionCategory category;

    private IdNameEmbeddable operationRegion;

    public RegionOfLocationResponse() {

    }

    public RegionOfLocationResponse(RegionCategory category, DistributionRegion distributionRegion, OperationRegion operationRegion) {
        this.id = distributionRegion.getId();
        this.name = distributionRegion.getName();
        this.category = category;
        this.operationRegion = new IdNameEmbeddable(operationRegion.getId(), operationRegion.getName());
    }

    public RegionOfLocationResponse(RegionCategory category, CollectionRegion collectionRegion, OperationRegion operationRegion) {
        this.id = collectionRegion.getId();
        this.name = collectionRegion.getName();
        this.category = category;
        this.operationRegion = new IdNameEmbeddable(operationRegion.getId(), operationRegion.getName());
    }

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

    public IdNameEmbeddable getOperationRegion() {
        return operationRegion;
    }

    public void setOperationRegion(IdNameEmbeddable operationRegion) {
        this.operationRegion = operationRegion;
    }

    public RegionCategory getCategory() {
        return category;
    }

    public void setCategory(RegionCategory category) {
        this.category = category;
    }
}
