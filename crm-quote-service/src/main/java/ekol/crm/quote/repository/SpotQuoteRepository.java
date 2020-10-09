package ekol.crm.quote.repository;

import java.time.*;
import java.util.*;

import ekol.crm.quote.domain.enumaration.QuoteStatus;
import ekol.crm.quote.domain.model.quote.*;
import ekol.hibernate5.domain.repository.ApplicationRepository;

public interface SpotQuoteRepository extends ApplicationRepository<SpotQuote> {
	
	Optional<SpotQuote> findById(Long id);

    Optional<SpotQuote> findByReferrerTaskId(String referrerTaskId);

    List<SpotQuote> findAllByStatusAndHoldingForCompanyTransferTrueAndAccountLocationIdIn(QuoteStatus status, List<Long> locationIds);

    List<SpotQuote> findAllByStatusAndHoldingForCompanyTransferTrueAndPaymentRuleInvoiceLocationIdIn(QuoteStatus status, List<Long> locationIds);
    
    List<SpotQuote> findByContactId(Long contactId);
    
    Iterable<SpotQuote> findByStatusInAndValidityEndDate(Collection<QuoteStatus> statuses, LocalDate validityEndDate );
    
    List<SpotQuote> findByPotentialId(Long potentialId);
    
    List<SpotQuote> findByStatusAndServiceAreaAndLastUpdatedDateTimeBetween(QuoteStatus status, String serviceArea, LocalDateTime lastUpdatedDateTimeStart, LocalDateTime lastUpdatedDateTimeEnd );

}
