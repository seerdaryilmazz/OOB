package ekol.location.client.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.CodeNamePair;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhoneNumberWithType implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PhoneNumber phoneNumber;
	private CodeNamePair numberType;
	private CodeNamePair usageType;
	private boolean isDefault;

}
