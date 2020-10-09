package ekol.crm.opportunity.domain.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.opportunity.domain.dto.MonetaryAmountJson;
import ekol.crm.opportunity.domain.model.product.Product;
import ekol.crm.opportunity.domain.model.product.WarehouseProduct;
import ekol.exceptions.ValidationException;
import ekol.model.IdNamePair;
import ekol.model.IsoNamePair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Dogukan Sahinturk on 9.01.2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WarehouseProductJson extends ProductJson {

    @NotNull(message = "Country can not be empty")
    private IsoNamePair country;
    private IdNamePair point;

    public Product toEntity() {
        WarehouseProduct entity = new WarehouseProduct();
        entity.setId(getId());
        entity.setExistenceType(getExistenceType());
        entity.setExpectedTurnoverPerYear(Optional.ofNullable(getExpectedTurnoverPerYear()).map(MonetaryAmountJson::toEntity).orElse(null));
        entity.setCountry(getCountry());
        entity.setPoint(getPoint());
        entity.setServiceArea(getServiceArea());
        return entity;
    }
}
