package ekol.crm.search.event;

import ekol.crm.search.event.dto.opportunity.OpportunityJson;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.search.event.dto.account.*;
import ekol.crm.search.event.dto.agreement.AgreementJson;
import ekol.crm.search.event.dto.quote.QuoteJson;
import ekol.crm.search.service.IndexingService;
import ekol.event.annotation.ConsumesWebEvent;

@RestController
@RequestMapping("/event-consumer")
public class EventConsumerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventConsumerController.class);

    @Autowired
    private IndexingService indexingService;

    @PostMapping("/account-update")
    @ConsumesWebEvent(event = "account-update", name = "index updated account to search engine")
    public void consumeAccountUpdate(@RequestBody AccountJson message){
        indexingService.indexAccount(message);
    }
    
    @PostMapping("/account-delete")
    @ConsumesWebEvent(event = "account-delete", name = "delete indexed account from search engine")
    public void consumeAccountDelete(@RequestBody AccountJson message){
    	indexingService.deleteAccount(message);
    }

    @PostMapping("/quote-search-index")
    @ConsumesWebEvent(event = "quote-search-index", name = "consume quote-search-index event in crm-search-service")
    public void consumeQuoteSearchIndexEvent(@RequestBody QuoteJson message) {
    	if(LOGGER.isInfoEnabled()) {
    		LOGGER.info("consuming quote-search-index event: {}", message.getId());
    	}
        indexingService.indexQuote(message);
    }

    @PostMapping("/contact-update")
    @ConsumesWebEvent(event = "contact-update", name = "index updated contact to search engine")
    public void consumeContactUpdate(@RequestBody ContactJson message){
        indexingService.indexContact(message);
    }

    @PostMapping("/contact-delete")
    @ConsumesWebEvent(event = "contact-delete", name = "index deleted contact from search engine")
    public void consumeContactDelete(@RequestBody ContactJson message){
        indexingService.deleteContact(message);
    }

    @PostMapping("/agreement-update")
    @ConsumesWebEvent(event = "agreement-update", name = "index updated agreement from search engine")
    public void consumeAgreementUpdate(@RequestBody AgreementJson message){
        indexingService.indexAgreement(message);
    }

    @PostMapping("/opportunity-update")
    @ConsumesWebEvent(event = "opportunity-update", name = "index updated opportunity from search engine")
    public void consumeOpportunityUpdate(@RequestBody OpportunityJson message){
        indexingService.indexOpportunity(message);
    }
}
