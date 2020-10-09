package ekol.crm.quote.domain.dto.pricingservice;

import java.math.BigDecimal;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.model.*;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Calculation {
	
	private QuoteInfo quoteInfo;
	
	private String collectionTariff;
	private String deliveryTariff;
	
	private IdNamePair subsidiary;
	private IdNamePair warehouse;
	private IsoNamePair fromCountry;
	private String fromPostal;
	private IsoNamePair toCountry;
	private String toPostal;
	private BigDecimal payweight;
	private BigDecimal grossWeight;
	private String currency = "EUR";
	private Set<IdCodeName> billingItems = Collections.emptySet();
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor(staticName = "with")
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class QuoteInfo {
		private Long id;
		private Long number;
		private String name;
		private IdNamePair account;
	}
}
