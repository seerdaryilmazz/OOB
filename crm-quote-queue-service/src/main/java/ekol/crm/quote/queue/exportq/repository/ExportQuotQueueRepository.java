package ekol.crm.quote.queue.exportq.repository;

import java.util.List;

import ekol.crm.quote.queue.exportq.domain.ExportQuoteQueueItem;
import ekol.mongodb.domain.repository.ApplicationMongoRepository;

public interface ExportQuotQueueRepository extends ApplicationMongoRepository<ExportQuoteQueueItem> {
	List<ExportQuoteQueueItem> findByQuoteId(Long quoteId);
	List<ExportQuoteQueueItem> findByQuoteNumber(Long quoteNumber);
	
	ExportQuoteQueueItem findFirstByQuoteNumberOrderByQuoteRevisionNumberDesc(Long quoteNumber);
	
}
