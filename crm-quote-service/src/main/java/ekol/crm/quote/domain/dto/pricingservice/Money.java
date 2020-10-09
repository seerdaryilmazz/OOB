package ekol.crm.quote.domain.dto.pricingservice;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Money {
	private BigDecimal amount;
	private String currency;
}
