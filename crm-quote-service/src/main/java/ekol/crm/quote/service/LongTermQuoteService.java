package ekol.crm.quote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.crm.quote.domain.EmailJson;
import ekol.crm.quote.domain.dto.kartoteksservice.Company;
import ekol.crm.quote.domain.model.quote.LongTermQuote;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.exceptions.BadRequestException;

@Service
public class LongTermQuoteService extends AbstractQuoteService {

	@Autowired
	private BusinessVolumeService businessVolumeService;
	
	@Override
	protected void saveRelationsForQuote(Quote existed, Quote quote) {
		super.saveRelationsForQuote(existed, quote);
		LongTermQuote existedLongTermQuote = LongTermQuote.class.cast(existed);
		LongTermQuote longTermQuote = LongTermQuote.class.cast(quote);
		longTermQuote.setBusinessVolume(businessVolumeService.save(existedLongTermQuote, longTermQuote.getBusinessVolume()));
	}

	@Override
	public void emailQuote(Quote quote, EmailJson request) {
		throw new BadRequestException("This operation is supported for only spot and tender quotes.");
	}

	@Override
	protected void createPdf(Quote quote) {
		// TODO Auto-generated method stub
	}
	
}
