package ekol.location.client.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Company implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
    private String name;
	private List<CompanyLocation> companyLocations;
}
