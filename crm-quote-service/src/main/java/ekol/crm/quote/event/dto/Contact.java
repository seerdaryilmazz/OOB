package ekol.crm.quote.event.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.IdNamePair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

 @JsonIgnoreProperties(ignoreUnknown = true)
 @Getter
 @Setter
 @Builder
 @NoArgsConstructor
 @AllArgsConstructor
 public class Contact {

	private Long id;
	private IdNamePair account;
	private Long companyContactId;
	private String firstName;
	private String lastName;
	    
	public String getFullname() {
	   return getFirstName() + " " + getLastName();
	
	}
	

}
