package ekol.crm.quote.domain.dto.pricingservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "with")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Freight {

	private	QueryResult collection;
	private	QueryResult delivery;
	private	QueryResult linehaul;
	
	private Money amount; 
}
