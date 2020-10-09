package ekol.location.client.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneNumber implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String countryCode;
    private String regionCode;
    private String phone;
    private String extension;
}
