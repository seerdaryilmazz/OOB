package ekol.crm.quote.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ekol.crm.quote.domain.EmailJson;
import ekol.crm.quote.domain.dto.DocumentJson;
import ekol.crm.quote.domain.dto.NoteJson;
import ekol.crm.quote.domain.dto.QuoteIdMappingJson;
import ekol.crm.quote.domain.dto.QuoteOrderMappingJson;
import ekol.crm.quote.domain.model.Document;
import ekol.crm.quote.domain.model.Note;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.crm.quote.domain.model.quote.SpotQuote;
import ekol.model.IdNamePair;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QuoteService {
	
	private QuoteServiceFactory quoteServiceFactory;
	
	public Quote save(Quote quote) {
		return quoteServiceFactory.getService(quote.getClass()).save(quote);
	}
	
	public Quote update(Long id, Quote quote) {
		return quoteServiceFactory.getService(quote.getClass()).update(id, quote);
	}

	public Quote getById(Long id) {
		return quoteServiceFactory.getService().getById(id);
	}

	public Quote getByNumber(Long number) {
		return quoteServiceFactory.getService().getByNumberWithExtra(number);
	}

	public Quote getByReferrerTask(String referrerTaskId) {
		return SpotQuoteService.class.cast(quoteServiceFactory.getService(SpotQuote.class)).getByReferrerTask(referrerTaskId);
	}
	
	public Page<Quote> list(Pageable pageRequest) {
		return quoteServiceFactory.getService().list(pageRequest);
	}

	public Collection<Note> updateNotes(Long quoteId, List<NoteJson> request) {
		return quoteServiceFactory.getService().updateNotes(quoteId, request);
	}
	
	public Collection<Document> updateDocuments(Long quoteId, List<DocumentJson> request) {
		return quoteServiceFactory.getService().updateDocuments(quoteId, request);
	}
	
	public void emailQuote(Long quoteId, EmailJson request) {
		Quote quote = quoteServiceFactory.getService().getById(quoteId);
		quoteServiceFactory.getService(quote.getClass()).emailQuote(quote, request);
	}

	public Quote updateApplicationIdMapping(Long number, QuoteIdMappingJson request) {
		return quoteServiceFactory.getService().updateApplicationIdMapping(number, request);
	}
	
	public Quote updateOrderMapping(Long number, QuoteOrderMappingJson request) {
		return quoteServiceFactory.getService().updateOrderMapping(number, request);
	}
	
	public boolean ensureCompanyAndLocations(Quote quote) {
		return quoteServiceFactory.getService(quote.getClass()).ensureCompanyAndLocations(quote);
	}
	
	public Quote updateSimple(Quote quote) {
		return quoteServiceFactory.getService(quote.getClass()).updateSimple(quote);
	}
	
	public Iterable<Quote> findByAccountId(Long accountId) {
		return quoteServiceFactory.getService().listByAccountId(accountId);
	}
	
	public Iterable<Quote> findAll(Collection<Long> ids) {
		return quoteServiceFactory.getService().listAll(ids);
	}
	
	public void updateAccount(Collection<Long> ids, IdNamePair account) {
		quoteServiceFactory.getService().updateAccount(ids, account);
	}
	public List<Quote> findAllByAttribute(String attributeKey, String attributeValue){
		return quoteServiceFactory.getService().findByAttribute(attributeKey, attributeValue);
	}
}
