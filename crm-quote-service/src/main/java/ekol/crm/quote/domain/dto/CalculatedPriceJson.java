package ekol.crm.quote.domain.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ekol.model.IdCodeName;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "with")
public class CalculatedPriceJson {
	private IdCodeName profit;
	@JsonInclude(Include.NON_NULL)
	private CostBasedFreightDetailJson unitPrice;
	private List<PriceJson> prices;
	
	public static CalculatedPriceJson with(IdCodeName profit, List<PriceJson> prices) {
		return CalculatedPriceJson.with(profit, null, prices);
	}
}
