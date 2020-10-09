package ekol.crm.quote.domain.annotation;

import java.lang.annotation.*;

import ekol.crm.quote.service.AbstractQuoteService;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface QuoteService {
	Class<? extends AbstractQuoteService> value();
}
