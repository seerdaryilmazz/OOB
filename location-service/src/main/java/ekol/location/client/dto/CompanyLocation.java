package ekol.location.client.dto;

import java.io.Serializable;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyLocation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id ;
    private String name;
    private String shortName;
    private boolean pointOnMapConfirmed;
    private PostalAddress postaladdress;
    private List<PhoneNumberWithType> phoneNumbers;
    private boolean active;
}
