package ekol.crm.configuration.domain;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.*;
import ekol.mongodb.domain.entity.BaseEntity;
import lombok.*;

@Getter
@Setter
@Document(collection = "configuration")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Configuration extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IdNamePair subsidiary;
	
	@DBRef
	private ConfigurationKey key;
	private Object value;
	private ParameterStatus status;
}
