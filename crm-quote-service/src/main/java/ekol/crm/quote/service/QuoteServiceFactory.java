package ekol.crm.quote.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import ekol.crm.quote.domain.annotation.QuoteService;
import ekol.crm.quote.domain.model.quote.*;
import ekol.exceptions.BadRequestException;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QuoteServiceFactory {
	
	private ApplicationContext applicationContext;
	
	public AbstractQuoteService getService(Class<? extends Quote> clazz) {
		if(Objects.isNull(clazz)) {
			throw new BadRequestException("Quote Service lookup fail!"); 
		}
		return applicationContext.getBean(clazz.getAnnotation(QuoteService.class).value());
	}
	
	public AbstractQuoteService getService() {
		return getService(SpotQuote.class);
	}
}
