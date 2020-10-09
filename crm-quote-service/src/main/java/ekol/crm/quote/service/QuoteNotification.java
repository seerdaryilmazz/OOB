package ekol.crm.quote.service;

import java.time.*;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ekol.crm.quote.domain.enumaration.QuoteStatus;
import ekol.crm.quote.domain.model.QuoteOrderMapping;
import ekol.crm.quote.domain.model.quote.SpotQuote;
import ekol.crm.quote.repository.SpotQuoteRepository;
import ekol.model.CodeNamePair;
import ekol.notification.api.NotificationApi;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QuoteNotification {

	private NotificationApi notificationApi;
	private SpotQuoteRepository spotQuoteRepository;

	public List<CodeNamePair> proceedExpiringQuotesNotification(Integer remainingDays, String concern) {
		Iterable<SpotQuote> quotes = spotQuoteRepository.findByStatusInAndValidityEndDate(Arrays.asList(QuoteStatus.OPEN, QuoteStatus.PDF_CREATED), LocalDate.now().plusDays(remainingDays));
		quotes.forEach(quote -> notificationApi.sendNotification(concern, quote.toJson(), quote.getCreatedBy()));
		return StreamSupport.stream(quotes.spliterator(), true).map(quote->CodeNamePair.with(String.valueOf(quote.getNumber()), quote.getName())).collect(Collectors.toList());
	}
	
	public List<CodeNamePair> proceedWonQuotesNoOrderNotification(Integer beforeDays, String concern){
		LocalDateTime start = LocalDate.now().minusDays(beforeDays).atStartOfDay();
		LocalDateTime end = start.plusDays(1);
		Iterable<SpotQuote>  quotes = StreamSupport.stream(spotQuoteRepository.findByStatusAndServiceAreaAndLastUpdatedDateTimeBetween(QuoteStatus.WON, "ROAD", start, end).spliterator(), true)
				.filter(t->CollectionUtils.isEmpty(t.getOrders()) || t.getOrders().parallelStream().map(QuoteOrderMapping::getOrderRelation).noneMatch("ADDED"::equals))
				.filter(t->CollectionUtils.isNotEmpty(t.getMappedIds()))
				.collect(Collectors.toList());
		quotes.forEach(quote -> notificationApi.sendNotification(concern, quote.toJson(), quote.getCreatedBy()));
		return StreamSupport.stream(quotes.spliterator(), true).map(quote->CodeNamePair.with(String.valueOf(quote.getNumber()), quote.getName())).collect(Collectors.toList());
	}
}
