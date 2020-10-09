package ekol.crm.quote.domain.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.crm.quote.domain.model.*;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PayWeightCalculationJson {
	private BigDecimal payWeight;
	private BigDecimal volumeCoefficient;
	private BigDecimal ldmCoefficient;
	
	public PayWeightCalculation toEntity(){
        return PayWeightCalculation.builder()
        		.ldmCoefficient(ldmCoefficient)
        		.volumeCoefficient(volumeCoefficient)
        		.build();
    }

    public static PayWeightCalculationJson fromEntity(PayWeightCalculation payWeightCalculation){
        return PayWeightCalculationJson.builder()
        		.ldmCoefficient(payWeightCalculation.getLdmCoefficient())
        		.volumeCoefficient(payWeightCalculation.getVolumeCoefficient())
        		.build();
    }
}
