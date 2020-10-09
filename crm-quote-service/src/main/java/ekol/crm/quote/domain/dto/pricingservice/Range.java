package ekol.crm.quote.domain.dto.pricingservice;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Range<T extends Serializable> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private T minimum;
	private T maximum;
}
