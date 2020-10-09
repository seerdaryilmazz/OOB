package ekol.orders.order.builder;

import ekol.model.CodeNamePair;
import ekol.model.IdNamePair;
import ekol.orders.order.domain.dto.response.location.RegionResponse;

public final class RegionResponseBuilder {
    private Long id;
    private String name;
    private CodeNamePair category;
    private IdNamePair operationRegion;

    private RegionResponseBuilder() {
    }

    public static RegionResponseBuilder aRegionResponse() {
        return new RegionResponseBuilder();
    }

    public RegionResponseBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public RegionResponseBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RegionResponseBuilder withCategory(CodeNamePair category) {
        this.category = category;
        return this;
    }

    public RegionResponseBuilder withOperationRegion(IdNamePair operationRegion) {
        this.operationRegion = operationRegion;
        return this;
    }

    public RegionResponseBuilder but() {
        return aRegionResponse().withId(id).withName(name).withCategory(category).withOperationRegion(operationRegion);
    }

    public RegionResponse build() {
        RegionResponse regionResponse = new RegionResponse();
        regionResponse.setId(id);
        regionResponse.setName(name);
        regionResponse.setCategory(category);
        regionResponse.setOperationRegion(operationRegion);
        return regionResponse;
    }
}
