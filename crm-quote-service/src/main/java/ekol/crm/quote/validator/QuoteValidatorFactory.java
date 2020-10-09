package ekol.crm.quote.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;

import ekol.crm.quote.domain.model.quote.*;
import ekol.crm.quote.util.BeanUtils;
import ekol.exceptions.BadRequestException;

@Component
public class QuoteValidatorFactory {
	public AbstractQuoteValidator getService(Class<? extends Quote> clazz) {
		if(Objects.isNull(clazz)) {
			throw new BadRequestException("Quote Validator lookup fail!"); 
		}
		
		if(TenderQuote.class.isAssignableFrom(clazz)) {
			return BeanUtils.getBean(TenderQuoteValidator.class);
		} else if(LongTermQuote.class.isAssignableFrom(clazz)) {
			return BeanUtils.getBean(LongTermQuoteValidator.class);
		}
		return BeanUtils.getBean(SpotQuoteValidator.class);
	}
}
