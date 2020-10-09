package ekol.crm.opportunity.event;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.opportunity.domain.dto.*;
import ekol.crm.opportunity.domain.enumaration.OpportunityStatus;
import ekol.crm.opportunity.domain.model.Opportunity;
import ekol.crm.opportunity.event.dto.AccountMerge;
import ekol.crm.opportunity.service.*;
import ekol.event.annotation.ConsumesWebEvent;
import lombok.AllArgsConstructor;


/**
 * Created by Dogukan Sahinturk on 4.12.2019
 */
@RestController
@RequestMapping("/event-consumer")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventConsumerController {

	private static final String OPPORTUNITY_ATTR_KEY = "opportunity";
	private static final String CANCELED = "CANCELED";
	
    private OpportunityService opportunityService;
    private CrmQuoteService quoteService;

    @PostMapping("/quote-update")
    @ConsumesWebEvent(event = "quote-update", name = "update quote calculate turnover")
    public void consumeQuoteUpdate(@RequestBody QuoteJson message) {
        if (null == message.getQuoteAttribute().get(OPPORTUNITY_ATTR_KEY)) {
        	return;
        }
        final Long opportunityId = Long.valueOf(message.getQuoteAttribute().get(OPPORTUNITY_ATTR_KEY));
        OpportunityJson opportunityJson = opportunityService.getOpportunityById(opportunityId);
        Supplier<Stream<QuoteJson>> quoteStream = ()->quoteService.getQuotesCreatedFromOpportunity(opportunityId, true).stream();
		if( OpportunityStatus.QUOTED == opportunityJson.getStatus() && quoteStream.get().allMatch(t->Objects.equals(CANCELED, t.getStatus().getCode()))) {
			opportunityJson.setStatus(OpportunityStatus.PROSPECTING);
		} else {
			opportunityJson.setStatus(OpportunityStatus.QUOTED);
		}
        
        opportunityJson = opportunityService.calculateQuotedTurnover(message, opportunityJson);

        if(Objects.nonNull(message.getPreviousData())){
            if (Stream.of("WON", "PARTIAL_WON").anyMatch(message.getPreviousData().getStatus().getCode()::equals) && "OPEN".equals(message.getStatus().getCode())) {
                opportunityJson = opportunityService.calculateCommittedTurnover(message, "OPEN", opportunityJson);
            } else if (Stream.of("WON", "PARTIAL_WON").anyMatch(message.getStatus().getCode()::equals)) {
                opportunityJson = opportunityService.calculateCommittedTurnover(message, "WON", opportunityJson);
            }
        }
        opportunityService.save(opportunityJson);
    }

    @PostMapping("/account-merge")
    @ConsumesWebEvent(event="account-merge", name = "consume opportunities in account merge")
    public void consumeMergeAccountEvent(@RequestBody AccountMerge accountMerge) {
    	if(!Objects.isNull(accountMerge.getOpportunities())) {
            Set<Long> opportunityIds = accountMerge.getOpportunities().stream().map(OpportunityJson:: getId).collect(Collectors.toSet());
            opportunityService.updateAccount(opportunityIds,accountMerge.getAccount());   
    	}

        List<Opportunity> opportunities = opportunityService.getByAccountId(accountMerge.getDeletedAccount().getId());
        
        if(!Objects.isNull(opportunities)) { 
        	opportunityService.deleteOpportunity(opportunities);
        }
    }
}
