package ekol.crm.quote.domain.dto.pricingservice;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.IdCodeName;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryResult {
	
	private Long id;
	private String name;
	private String currency;
	private BigDecimal unitPrice;
	private BigDecimal price;
	private Range<BigDecimal> priceLimit;
	private Range<BigDecimal> payweight;
	private BigDecimal totalAmount;
}
