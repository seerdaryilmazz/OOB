package ekol.location.client.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String iso;
}
