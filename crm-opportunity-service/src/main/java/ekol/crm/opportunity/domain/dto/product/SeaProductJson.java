package ekol.crm.opportunity.domain.dto.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.crm.opportunity.domain.model.product.DomesticProduct;
import ekol.crm.opportunity.domain.model.product.Product;
import ekol.crm.opportunity.domain.model.product.SeaProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Dogukan Sahinturk on 9.01.2020
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeaProductJson extends FreightProductJson {


}
