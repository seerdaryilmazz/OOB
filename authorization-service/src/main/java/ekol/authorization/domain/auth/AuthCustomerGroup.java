package ekol.authorization.domain.auth;

import org.neo4j.ogm.annotation.NodeEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
@NodeEntity(label = "CustomerGroup")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthCustomerGroup extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4935106003879679703L;
	
}
