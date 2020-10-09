package ekol.crm.opportunity.domain.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.opportunity.domain.dto.MonetaryAmountJson;
import ekol.crm.opportunity.domain.model.product.CustomsProduct;
import ekol.crm.opportunity.domain.model.product.Product;
import ekol.exceptions.ValidationException;
import ekol.model.IdNamePair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Created by Dogukan Sahinturk on 9.01.2020
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomsProductJson extends ProductJson {

    @NotNull(message = "Customs service type can not be empty")
    private String customsServiceType;
    private IdNamePair customsOffice;


    public Product toEntity(){
    CustomsProduct entity = new CustomsProduct();
    entity.setId(getId());
    entity.setExistenceType(getExistenceType());
    entity.setExpectedTurnoverPerYear(Optional.ofNullable(getExpectedTurnoverPerYear()).map(MonetaryAmountJson::toEntity).orElse(null));
    entity.setCustomsServiceType(getCustomsServiceType());
    entity.setCustomsOffice(getCustomsOffice());
    entity.setServiceArea(getServiceArea());
    return entity;

    }
}
