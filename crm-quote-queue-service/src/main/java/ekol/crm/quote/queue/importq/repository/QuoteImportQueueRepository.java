package ekol.crm.quote.queue.importq.repository;

import ekol.crm.quote.queue.importq.domain.ImportQuoteQueueItem;
import ekol.mongodb.domain.repository.ApplicationMongoRepository;

public interface QuoteImportQueueRepository extends ApplicationMongoRepository<ImportQuoteQueueItem> {
	ImportQuoteQueueItem findByQuoteNumberAndExternalSystemName(Long quoteNumber, String externalSystemName);
}
