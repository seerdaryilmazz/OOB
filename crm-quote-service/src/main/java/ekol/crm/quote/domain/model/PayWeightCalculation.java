package ekol.crm.quote.domain.model;

import java.math.BigDecimal;

import javax.persistence.*;

import lombok.*;

@Embeddable
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PayWeightCalculation {
	
	@Column
	private BigDecimal volumeCoefficient;
	
	@Column
	private BigDecimal ldmCoefficient;
}
