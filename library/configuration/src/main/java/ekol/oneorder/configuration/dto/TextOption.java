package ekol.oneorder.configuration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TextOption extends Option<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TextOption() {
		super();
		setValue("");
	}

}
