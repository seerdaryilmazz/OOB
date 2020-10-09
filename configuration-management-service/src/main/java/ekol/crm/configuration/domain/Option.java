package ekol.crm.configuration.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.IdNamePair;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Option implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String key;
	private Object value;
	private String valueType;
	private IdNamePair subsidiary;
	
	public static Option from(Configuration configuration) {
		Option option = new Option();
		option.setSubsidiary(configuration.getSubsidiary());
		option.setKey(configuration.getKey().getCode());
		option.setValue(configuration.getValue());
		option.setValueType(configuration.getKey().getValueType().name());
		return option;
	}
}
