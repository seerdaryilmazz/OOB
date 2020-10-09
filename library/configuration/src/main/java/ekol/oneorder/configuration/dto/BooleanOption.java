package ekol.oneorder.configuration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BooleanOption extends Option<Boolean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BooleanOption() {
		super();
		setValue(Boolean.FALSE);
	}

}
