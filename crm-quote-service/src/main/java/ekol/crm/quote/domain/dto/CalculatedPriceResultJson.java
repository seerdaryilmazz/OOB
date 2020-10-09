package ekol.crm.quote.domain.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "with")
public class CalculatedPriceResultJson {
	private CalculatedPriceJson[] calculatedPrices;
	private String calculationTariff;
}
