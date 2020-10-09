package ekol.crm.opportunity.domain.model.product;

import ekol.crm.opportunity.domain.dto.MonetaryAmountJson;
import ekol.crm.opportunity.domain.dto.product.ProductJson;
import ekol.crm.opportunity.domain.dto.product.WarehouseProductJson;
import ekol.model.IdNamePair;
import ekol.model.IsoNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Dogukan Sahinturk on 9.01.2020
 */
@Entity
@DiscriminatorValue(value = "WHM")
@Getter
@Setter
@NoArgsConstructor
public class WarehouseProduct extends Product {

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="iso", column=@Column(name="COUNTRY_ISO")),
            @AttributeOverride(name = "name", column=@Column(name="COUNTRY_NAME"))})
    private IsoNamePair country;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="POINT_ID")),
            @AttributeOverride(name = "name", column=@Column(name="POINT_NAME"))})
    private IdNamePair point;

    @Override
    public ProductJson toJson() {
        WarehouseProductJson json = new WarehouseProductJson();
        json.setId(getId());
        json.setExistenceType(getExistenceType());
        json.setExpectedTurnoverPerYear(MonetaryAmountJson.fromEntity(getExpectedTurnoverPerYear()));
        json.setCountry(getCountry());
        json.setPoint(getPoint());
        json.setServiceArea(getServiceArea());
        return json;
    }
}
