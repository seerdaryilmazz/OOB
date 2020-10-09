package ekol.crm.quote.repository;

import java.util.Optional;

import ekol.crm.quote.domain.model.QuoteOrderMapping;
import ekol.hibernate5.domain.repository.ApplicationRepository;

public interface QuoteOrderMappingRepository extends ApplicationRepository<QuoteOrderMapping>{
	Optional<QuoteOrderMapping> findByQuoteNumberAndOrderNumber(Long number, String orderNumber);
}
