package ekol.crm.quote.repository;

import java.util.List;
import java.util.Optional;

import ekol.crm.quote.domain.model.quote.Quote;
import ekol.hibernate5.domain.repository.ApplicationRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuoteRepository extends ApplicationRepository<Quote> {
    Optional<Quote> findById(Long id);
    Optional<Quote> findByNumber(Long number);
    Iterable<Quote> findByAccountId(Long accountId);

    @Query(value = "select q from Quote q inner join q.quoteAttribute qa where KEY(qa)=:ATTR_KEY and VALUE(qa)=:ATTR_VALUE")
    List<Quote> findQuotesByQuoteAttributes(@Param("ATTR_KEY") String ATTR_KEY, @Param("ATTR_VALUE") String ATTR_VALUE);

}
