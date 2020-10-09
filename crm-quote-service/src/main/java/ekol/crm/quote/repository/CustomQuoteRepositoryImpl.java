package ekol.crm.quote.repository;

import javax.persistence.*;

import org.springframework.stereotype.Repository;

import ekol.crm.quote.domain.model.quote.Quote;

@Repository
public class CustomQuoteRepositoryImpl implements CustomQuoteRepository {

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public void detach(Quote quote) {
		entityManager.detach(quote);
	}
}
