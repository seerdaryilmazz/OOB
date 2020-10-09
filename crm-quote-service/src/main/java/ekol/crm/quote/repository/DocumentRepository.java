package ekol.crm.quote.repository;

import ekol.crm.quote.domain.model.Document;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;

public interface DocumentRepository extends ApplicationRepository<Document> {

    List<Document> findByQuote(Quote quote);
}
