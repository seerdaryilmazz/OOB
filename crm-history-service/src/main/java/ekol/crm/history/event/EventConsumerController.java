package ekol.crm.history.event;


import ekol.crm.history.domain.History;
import ekol.crm.history.domain.Item;
import ekol.crm.history.domain.dto.type.*;
import ekol.crm.history.event.dto.account.AccountJson;
import ekol.crm.history.event.dto.agreement.AgreementExtensionJson;
import ekol.crm.history.event.dto.agreement.AgreementJson;
import ekol.crm.history.event.dto.opportunity.OpportunityJson;
import ekol.crm.history.event.dto.potential.PotentialJson;
import ekol.crm.history.event.dto.quote.QuoteJson;
import ekol.crm.history.service.HistoryService;
import ekol.event.annotation.ConsumesWebEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("/event-consumer")
public class EventConsumerController {

    @Autowired
    private HistoryService historyService;

    @RequestMapping(value = "/account-update", method = RequestMethod.POST)
    @ConsumesWebEvent(event = "account-update", name = "update account change history")
    public void consumeAccountUpdate(@RequestBody AccountJson message){
        historyService.save(new Item(message.getId(), "account"), Account.fromJson(message), message.getLastUpdatedBy(), LocalDateTime.now());
    }

    @RequestMapping(value = "/potential-update", method = RequestMethod.POST)
    @ConsumesWebEvent(event = "potential-update", name = "update potential change history")
    public void consumePotentialUpdate(@RequestBody PotentialJson message){
        historyService.save(new Item(message.getId(), "potential"), Potential.fromJson(message), message.getLastUpdatedBy(), LocalDateTime.now());
    }

    @RequestMapping(value = "/quote-update", method = RequestMethod.POST)
    @ConsumesWebEvent(event = "quote-update", name = "update quote change history")
    public void consumeQuoteUpdate(@RequestBody QuoteJson message){
        historyService.save(new Item(message.getId(), "quote"), Quote.fromJson(message), message.getLastUpdatedBy(), LocalDateTime.now());
    }

    @RequestMapping(value = "/agreement-update", method = RequestMethod.POST)
    @ConsumesWebEvent(event = "agreement-update", name = "update agreement change history")
    public void consumeAgreementUpdate(@RequestBody AgreementJson message){
        History history = historyService.retrieveLatestData(message.getId(),"agreement");
        if(Objects.isNull(history)){
            historyService.save(new Item(message.getId(), "agreement"), Agreement.fromJson(message), message.getLastUpdatedBy(), LocalDateTime.now());
        }else{
            if(!(Agreement.class.cast(history.getLatestData()).getStatus().equals(message.getStatus().getCode()))){
                historyService.save(new Item(message.getId(), "agreement"), Agreement.fromJson(message), message.getLastUpdatedBy(), LocalDateTime.now());
            }
        }
    }

    @PostMapping("/agreement-extension")
    @ConsumesWebEvent(event = "agreement-extension", name = "agreement extension change history")
    public void consumeAgreementExtension(@RequestBody AgreementExtensionJson message){
        historyService.save(new Item(message.getAgreementId(), "agreement-extension"), AgreementExtension.fromJson(message), message.getChangedBy(), LocalDateTime.now());
    }

    @RequestMapping(value = "/opportunity-update", method = RequestMethod.POST)
    @ConsumesWebEvent(event = "opportunity-update", name = "update opportunity change history")
    public void consumeOpportunityUpdate(@RequestBody OpportunityJson message){
        historyService.save(new Item(message.getId(), "opportunity"), Opportunity.fromJson(message), message.getLastUpdatedBy(), LocalDateTime.now());
    }
}
