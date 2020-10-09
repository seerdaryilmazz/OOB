package ekol.crm.quote.repository;

import ekol.crm.quote.domain.model.quote.Quote;

public interface CustomQuoteRepository {
	void detach(Quote quote);
}
