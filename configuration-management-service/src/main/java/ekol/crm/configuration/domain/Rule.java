package ekol.crm.configuration.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rule {
	private String when;
	private Object then;
//	private int priority = 0;
//	
//	@Override
//	public int compareTo(Rule o) {
//		return Comparator.<Rule>comparingInt(Rule::getPriority).reversed().compare(this, o);
//	}
}
