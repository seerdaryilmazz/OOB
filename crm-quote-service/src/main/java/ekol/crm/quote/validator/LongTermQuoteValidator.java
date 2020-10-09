package ekol.crm.quote.validator;

import java.util.*;

import org.springframework.stereotype.Component;

import ekol.crm.quote.domain.model.quote.Quote;
import ekol.model.IdNamePair;

@Component
public class LongTermQuoteValidator extends AbstractQuoteValidator {

	@Override
	protected void validateQuote(Quote quote) {
	}
	
	@Override
	protected Map<Long, String> extractCompaniesId(Quote quote) {
		Map<Long, String> ids = new HashMap<>();
		Optional.of(quote).map(Quote::getAccountLocation).map(IdNamePair::getId).ifPresent(id->ids.put(id, "account"));
		return ids;
	}

}
