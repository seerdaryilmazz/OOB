package ekol.crm.quote.event;


import org.json.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import ekol.crm.quote.domain.dto.*;
import ekol.crm.quote.domain.dto.quote.QuoteJson;
import ekol.crm.quote.service.QuoteService;
import ekol.event.annotation.ProducesEvent;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class QuoteEventProducer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteEventProducer.class);
	
	private QuoteService quoteService;

    @ProducesEvent(event = "quote-update")
    @TransactionalEventListener(fallbackExecution = true, condition="#event.operation == T(ekol.crm.quote.event.QuoteEventOperation).UPDATED")
    public QuoteJson produce(QuoteEvent event) {
    	QuoteJson json = event.getData().toJson();
		json.setPreviousData(event.getPreviousData());
    	return json;
    }

    @ProducesEvent(event = "quote-search-index")
    @TransactionalEventListener(fallbackExecution = true)
    public QuoteJson produceQuoteSearchIndexEvent(QuoteSearchIndexEventMessage message) {
    	if(LOGGER.isWarnEnabled()) {
    		LOGGER.warn("producing quote-search-index event - quoteId: {}", message.getQuoteId());
    	}
        return quoteService.getById(message.getQuoteId()).toJson();
    }

    @ProducesEvent(event = "email-message-created")
    @TransactionalEventListener(fallbackExecution = true)
    public EmailMessageReturned produceEmailMessageCreated(EmailMessage message) {
            return new EmailMessageReturned(
                    message.getFrom(),
                    message.getReplyTo(),
                    message.getTo(),
                    message.getCc(),
                    message.getBcc(),
                    message.getSubject(),
                    message.getBody(),
                    message.getHtml(),
                    message.getAttachments());
    }

    @ProducesEvent(event = "force-company-export")
    @TransactionalEventListener(fallbackExecution = true)
    public ForceCompanyExportEventMessageReturned produceForceCompanyExportEvent(ForceCompanyExportEventMessage message) {
        return new ForceCompanyExportEventMessageReturned(message.getCompanyId());
    }

    @ProducesEvent(event = "force-location-export")
    @TransactionalEventListener(fallbackExecution = true)
    public ForceLocationExportEventMessageReturned produceForceLocationExportEvent(ForceLocationExportEventMessage message) {
    	if(LOGGER.isWarnEnabled()) {
    		LOGGER.warn("producing force-location-export event - locationId: {}", message.getLocationId());
    	}
        return new ForceLocationExportEventMessageReturned(message.getLocationId());
    }

    /**
     * Burası test amacıyla yapıldı.
     */
    @ProducesEvent(event = "company-import")
    @TransactionalEventListener(fallbackExecution = true)
    public CompanyImportEventMessage produceFakeCompanyImportEvent(FakeCompanyImportEventMessage message) {

        JSONArray locationArray = new JSONArray();

        for (Long locationId : message.getLocationIds()) {
            JSONObject locationObject = new JSONObject();
            locationObject.put("kartoteksId", locationId);
            locationArray.put(locationObject);
        }

        JSONObject rootObject = new JSONObject();
        rootObject.put("locations", locationArray);

        CompanyImportEventMessage importEventMessage = new CompanyImportEventMessage();
        importEventMessage.setData(rootObject.toString());

        return importEventMessage;
    }
}
