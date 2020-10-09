package ekol.crm.quote.domain.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.domain.model.QuoteOrderMapping;
import ekol.model.CodeNamePair;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteOrderMappingJson {
	private Long id;
	private String orderNumber;
	private CodeNamePair orderStatus;
	private String relation;
	
	public static QuoteOrderMappingJson fromEntity(QuoteOrderMapping entity) {
		if(Objects.isNull(entity)) {
			return null;
		}
		QuoteOrderMappingJson json = new QuoteOrderMappingJson();
		json.setId(entity.getId());
		json.setOrderNumber(entity.getOrderNumber());
		json.setOrderStatus(entity.getOrderStatus());
		json.setRelation(entity.getOrderRelation());
		return json;
	}
	
	public QuoteOrderMapping toEntity() {
		QuoteOrderMapping entity = new QuoteOrderMapping();
		entity.setId(getId());
		entity.setOrderNumber(getOrderNumber());
		entity.setOrderStatus(getOrderStatus());
		entity.setOrderRelation(getRelation());
		return entity;
	}
}
