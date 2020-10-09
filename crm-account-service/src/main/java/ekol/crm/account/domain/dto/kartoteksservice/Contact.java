package ekol.crm.account.domain.dto.kartoteksservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact {
	private Long id;
	private String firstName;
	private String lastName;
	
	public String getFullName() {
    	return getFirstName() + " " + getLastName();
    }
}
