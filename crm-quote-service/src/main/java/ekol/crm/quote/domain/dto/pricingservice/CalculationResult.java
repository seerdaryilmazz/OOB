package ekol.crm.quote.domain.dto.pricingservice;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.IdCodeName;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CalculationResult {
	private IdCodeName profit;
	private Freight freight;
	private Money totalFreightAmount;
	private Map<String, QueryResult> billingItems;
}
