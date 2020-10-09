package ekol.oneorder.configuration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.CodeNamePair;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListOption extends Option<CodeNamePair[]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ListOption() {
		super();
		setValue(new CodeNamePair[] {});
	}

}
