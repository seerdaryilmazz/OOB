package ekol.crm.search.domain.quote;

import org.springframework.data.elasticsearch.annotations.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.search.client.CrmAccountServiceClient;
import ekol.crm.search.event.dto.quote.ProductJson;
import ekol.crm.search.utils.BeanUtils;
import ekol.model.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSearchDoc {
	
	@Field(type = FieldType.Nested)
	private CodeNamePair shipmentLoadingType;
	
	@Field(type=FieldType.Nested)
	private IsoNamePair fromCountry;
	
	@Field(type=FieldType.Nested)
	private IsoNamePair toCountry;
	
	@Field(type=FieldType.Nested)
	private IdNamePair fromPoint;
	
	@Field(type=FieldType.Nested)
	private IdNamePair toPoint;
	
	public static ProductSearchDoc fromProduct(ProductJson product){
		
		return new ProductSearchDocBuilder()
			.shipmentLoadingType(BeanUtils.getBean(CrmAccountServiceClient.class).getShipmentLoadingTypeByCode(product.getShipmentLoadingType()))
			.fromCountry(product.getFromCountry())
			.toCountry(product.getToCountry())
			.fromPoint(product.getFromPoint())
			.toPoint(product.getToPoint())
			.build();
	}
	

}
