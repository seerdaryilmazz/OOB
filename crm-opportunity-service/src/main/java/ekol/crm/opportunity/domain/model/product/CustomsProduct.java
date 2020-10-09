package ekol.crm.opportunity.domain.model.product;

import ekol.crm.opportunity.domain.dto.MonetaryAmountJson;
import ekol.crm.opportunity.domain.dto.product.CustomsProductJson;
import ekol.crm.opportunity.domain.dto.product.ProductJson;
import ekol.model.IdNamePair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Dogukan Sahinturk on 9.01.2020
 */
@Entity
@DiscriminatorValue(value = "CCL")
@Getter
@Setter
@NoArgsConstructor
public class CustomsProduct extends Product {

    private String customsServiceType;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name="id", column=@Column(name="CUSTOMS_OFFICE_ID")),
            @AttributeOverride(name = "name", column=@Column(name="CUSTOMS_OFFICE_NAME"))})
    private IdNamePair customsOffice;

    @Override
    public ProductJson toJson() {
        CustomsProductJson json = new CustomsProductJson();
        json.setId(getId());
        json.setExistenceType(getExistenceType());
        json.setExpectedTurnoverPerYear(MonetaryAmountJson.fromEntity(getExpectedTurnoverPerYear()));
        json.setCustomsServiceType(getCustomsServiceType());
        json.setCustomsOffice(getCustomsOffice());
        json.setServiceArea(getServiceArea());
        return json;
    }
}
