package ekol.crm.quote.repository;

import java.util.List;

import ekol.crm.quote.domain.model.QuoteIdMapping;
import ekol.hibernate5.domain.repository.ApplicationRepository;

public interface QuoteIdMappingRepository extends ApplicationRepository<QuoteIdMapping> {
	public List<QuoteIdMapping> findByQuoteNumberAndApplication(Long number, String application);
}
