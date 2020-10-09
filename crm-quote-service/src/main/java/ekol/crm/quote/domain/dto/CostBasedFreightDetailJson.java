package ekol.crm.quote.domain.dto;

import ekol.crm.quote.domain.dto.pricingservice.Money;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "with")
public class CostBasedFreightDetailJson {
	private Money collection;
	private Money delivery;
	private Money linehaul;
}
