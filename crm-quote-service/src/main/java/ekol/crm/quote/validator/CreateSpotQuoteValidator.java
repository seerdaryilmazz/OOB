package ekol.crm.quote.validator;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import ekol.crm.quote.domain.dto.quote.*;
import ekol.exceptions.ValidationException;
import ekol.resource.validation.Validator;

@Component
public class CreateSpotQuoteValidator implements Validator {

	@Override
	public void validate(Object[] args) {
		Object quoteObj = args[0];
		QuoteJson quote = QuoteJson.class.cast(quoteObj);
		quote.validateQuote();
		if(quote instanceof SpotQuoteJson) {
			SpotQuoteJson spotQuoteJson = SpotQuoteJson.class.cast(quote);
			if(LocalDate.now().isAfter(spotQuoteJson.getValidityStartDate())) {
				throw new ValidationException("Validity start date must not be in past");
			}
		}

	}

}
