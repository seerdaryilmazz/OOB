package ekol.crm.quote.service;

import java.util.Objects;

import org.springframework.stereotype.Component;

import ekol.crm.quote.domain.dto.*;
import ekol.crm.quote.domain.dto.quote.*;
import ekol.crm.quote.domain.enumaration.*;

@Component
public class SpotQuoteNotificationConditon {
	
	public boolean isPriceRequestedBefore(QuoteJson quote) {
		if(!SpotQuoteJson.class.isInstance(quote)) {
			return false;
		}
		if(QuoteStatus.OPEN == quote.getStatus()) {
			return 0 < SpotQuoteJson.class.cast(quote)
					.getPrices()
					.stream()
					.map(PriceJson::getAuthorization)
					.filter(Objects::nonNull)
					.filter(t->Objects.nonNull(t.getRequestedAmount()))
					.filter(t->Objects.isNull(t.getCloseStatus()))
					.count();
		}
		return false;
	}
	
	public boolean isPriceApprovedBefore(QuoteJson quote) {
		if(!SpotQuoteJson.class.isInstance(quote)) {
			return false;
		}
		if(QuoteStatus.OPEN == quote.getStatus()) {
			return SpotQuoteJson.class.cast(quote).getPrices()
					.stream()
					.map(PriceJson::getAuthorization)
					.filter(Objects::nonNull)
					.map(PriceAuthorizationJson::getCloseStatus)
					.anyMatch(PriceAuthorizationStatus.REQUESTED::equals);
		}
		return false;
	}
	
	public boolean isPriceApprovedAfter(QuoteJson quote) {
		if(!SpotQuoteJson.class.isInstance(quote)) {
			return false;
		}
		if(QuoteStatus.OPEN == quote.getStatus()) {
			return SpotQuoteJson.class.cast(quote)
					.getPrices()
					.stream()
					.map(PriceJson::getAuthorization)
					.filter(Objects::nonNull)
					.map(PriceAuthorizationJson::getCloseStatus)
					.anyMatch(PriceAuthorizationStatus.APPROVED::equals);
		}
		return false;
	}

}
