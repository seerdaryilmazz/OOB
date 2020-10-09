package ekol.oneorder.configuration.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NumberOption extends Option<BigDecimal> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NumberOption() {
		super();
		setValue(BigDecimal.ZERO);
	}

}
