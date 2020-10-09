package ekol.location.client.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Point implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private BigDecimal lat;
    private BigDecimal lng;

}
