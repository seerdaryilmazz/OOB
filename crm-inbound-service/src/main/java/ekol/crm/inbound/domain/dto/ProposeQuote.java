package ekol.crm.inbound.domain.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import ekol.crm.inbound.domain.QuoteType;
import lombok.Data;

@Data
public class ProposeQuote {
	
	@NotNull(message = "Please select an account")
	private Long account;

	@NotNull(message = "Please select a quote type")
	private QuoteType quoteType;
	
	@NotBlank(message = "Please select a service area")
	private String serviceArea;
	
	@NotBlank(message = "inbound must be sent")
	private String inbound;
}
