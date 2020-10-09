package ekol.crm.opportunity.domain.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ekol.crm.opportunity.domain.dto.MonetaryAmountJson;
import ekol.crm.opportunity.domain.enumaration.ExistenceType;
import ekol.crm.opportunity.domain.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


/**
 * Created by Dogukan Sahinturk on 18.11.2019
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "serviceArea",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = WarehouseProductJson.class, name = "WHM"),
        @JsonSubTypes.Type(value = CustomsProductJson.class, name = "CCL"),
        @JsonSubTypes.Type(value = AirProductJson.class, name = "AIR"),
        @JsonSubTypes.Type(value = RoadProductJson.class, name = "ROAD"),
        @JsonSubTypes.Type(value = SeaProductJson.class, name = "SEA"),
        @JsonSubTypes.Type(value = DomesticProductJson.class, name = "DTR")
})
public abstract class ProductJson {
    private Long id;
    @NotNull(message = "Existence type can not be empty")
    private ExistenceType existenceType;
    private MonetaryAmountJson expectedTurnoverPerYear;
    @NotNull(message = "Product should have service area info")
    private String serviceArea;

    public abstract Product toEntity();

}
