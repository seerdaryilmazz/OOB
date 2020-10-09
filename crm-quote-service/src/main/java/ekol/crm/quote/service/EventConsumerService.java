package ekol.crm.quote.service;

import java.util.*;
import java.util.stream.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.crm.quote.domain.enumaration.QuoteStatus;
import ekol.crm.quote.domain.model.quote.*;
import ekol.crm.quote.event.*;
import ekol.crm.quote.repository.SpotQuoteRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventConsumerService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventConsumerService.class);

    private QuoteService quoteService;
    private SpotQuoteRepository spotQuoteRepository;
    private ApplicationEventPublisher publisher;

    @Transactional
    public void consumeCompanyImportEvent(CompanyImportEventMessage message) {
        List<Long> locationIds = extractLocationIds(message);

        if(LOGGER.isWarnEnabled()) {
        	LOGGER.warn("location ids extracted from company-import message: {}", StringUtils.join(locationIds, ","));
        }
        
        if (CollectionUtils.isNotEmpty(locationIds)) {
            // Aynı olanları elemek için map'e atıyoruz.
            Map<Long, SpotQuote> quoteMap = new HashMap<>();
            Optional.ofNullable(spotQuoteRepository.findAllByStatusAndHoldingForCompanyTransferTrueAndAccountLocationIdIn(QuoteStatus.WON, locationIds))
            	.map(Collection::stream)
            	.orElseGet(Stream::empty)
            	.forEach(quote->quoteMap.putIfAbsent(quote.getId(), quote));
            
            Optional.ofNullable(spotQuoteRepository.findAllByStatusAndHoldingForCompanyTransferTrueAndPaymentRuleInvoiceLocationIdIn(QuoteStatus.WON, locationIds))
	            .map(Collection::stream)
	            .orElseGet(Stream::empty)
            	.forEach(quote->quoteMap.putIfAbsent(quote.getId(), quote));

            for (SpotQuote quote : quoteMap.values()) {
                if (!quoteService.ensureCompanyAndLocations(quote)) {
                    quote.setHoldingForCompanyTransfer(Boolean.FALSE);
                    Quote entity = quoteService.updateSimple(quote);
                    publisher.publishEvent(QuoteEvent.with(entity, QuoteEventOperation.UPDATED));
                }
            }
        }
    }
    
    private List<Long> extractLocationIds(CompanyImportEventMessage message) {
        List<Long> locationIds = new ArrayList<>();
        Optional.ofNullable(message)
        	.map(CompanyImportEventMessage::getData)
        	.filter(StringUtils::isNotBlank)
        	.map(t->this.toJsonObject(t, true))
        	.map(t->this.getJsonArray(t, "locations", true))
        	.ifPresent(locationArray->
        		IntStream.range(0, locationArray.length()).parallel().forEach(i->
        			Optional.ofNullable(getJsonObjectAtIndex(locationArray, i, true))
        				.map(locationObject->getPropertyAsLong(locationObject, "kartoteksId", true))
        				.ifPresent(locationIds::add)
        		)
        	);
        return locationIds;
    }

    private JSONObject toJsonObject(String json, boolean ignoreException) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            if (!ignoreException) {
                throw e;
            }
        }
        return jsonObject;
    }

    private JSONArray getJsonArray(JSONObject jsonObject, String key, boolean ignoreException) {
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray(key);
        } catch (JSONException e) {
            if (!ignoreException) {
                throw e;
            }
        }
        return jsonArray;
    }

    private JSONObject getJsonObjectAtIndex(JSONArray jsonArray, int index, boolean ignoreException) {
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(index);
        } catch (JSONException e) {
            if (!ignoreException) {
                throw e;
            }
        }
        return jsonObject;
    }

    private Long getPropertyAsLong(JSONObject jsonObject, String key, boolean ignoreException) {
        Long value = null;
        try {
            value = jsonObject.getLong(key);
        } catch (JSONException e) {
            if (!ignoreException) {
                throw e;
            }
        }
        return value;
    }
}
