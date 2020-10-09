package ekol.crm.quote.event;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.crm.quote.event.dto.AccountMerge;
import ekol.crm.quote.event.dto.Contact;
import ekol.crm.quote.service.EventConsumerService;
import ekol.crm.quote.service.QuoteService;
import ekol.crm.quote.service.SpotQuoteService;
import ekol.event.annotation.ConsumesWebEvent;
import ekol.model.IdNamePair;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/event-consumer")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventConsumerController {

    private EventConsumerService eventConsumerService;
    private SpotQuoteService spotQuoteService;
    private QuoteService quoteService;
    
    @PostMapping(value = "/company-import")
    @ConsumesWebEvent(event = "company-import", name = "consume company-import event in crm-quote-service")
    public void consumeCompanyImportEvent(@RequestBody CompanyImportEventMessage message) {
        eventConsumerService.consumeCompanyImportEvent(message);
    }
    
    @PostMapping(value="/contact-update")
    @ConsumesWebEvent(event= "contact-update", name ="quote contact update")
    public void consumeAccountContactUpdate(@RequestBody Contact message) {
    	spotQuoteService.updateQuoteContactName(message);
    }
    
    @PostMapping(value="/account-merge")
    @ConsumesWebEvent(event= "account-merge", name= "consume quotes in account merge")
    public void consumeMergeAccountEvent(@RequestBody AccountMerge accountMerge) {
    	Set<Long> quoteIds = accountMerge.getQuotes().stream().map(IdNamePair::getId).collect(Collectors.toSet());
    	quoteService.updateAccount(quoteIds, accountMerge.getAccount());
    }
}
