package ekol.crm.quote.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.IdNamePair;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class PayWeightCalculationParams {

	private Long quoteId;
	private IdNamePair subsidiary;
    private BigDecimal weight;
    private BigDecimal ldm;
    private BigDecimal volume;
    private Boolean speedyService;
    private Boolean speedyvanService;
    
}
