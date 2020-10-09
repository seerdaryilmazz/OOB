package ekol.crm.account.event;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.account.domain.dto.QuoteJson;
import ekol.crm.account.domain.enumaration.AccountType;
import ekol.crm.account.domain.model.Account;
import ekol.crm.account.event.dto.CompanyUpdatedEventMessage;
import ekol.crm.account.repository.CustomAccountRepository;
import ekol.crm.account.service.*;
import ekol.event.annotation.ConsumesWebEvent;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/event-consumer")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EventConsumerController {

    private AccountCrudService accountCrudService;
    private ContactCrudService contactCrudService;
    private CompanyService companyService;
    private CustomAccountRepository customAccountRepository;

    @PostMapping("/quote-update")
    @ConsumesWebEvent(event = "quote-update", name = "account type has been updated to customer")
    public void consumeWinnedQuote(@RequestBody QuoteJson message){
    	if(Stream.of("WON","PARTIAL_WON").anyMatch(message.getStatus().getCode()::equals)) {
    		Account account = accountCrudService.getByIdOrThrowException(message.getAccount().getId());
    		customAccountRepository.detach(account);
    		if(AccountType.PROSPECT == account.getAccountType()){
    			accountCrudService.updateAccountType(account, AccountType.CUSTOMER);
    		}
    	}
    }

    @PostMapping("/company-updated")
    @ConsumesWebEvent(event = "company-updated", name = "consume company-updated event in crm-account-service")
    public void consumeCompanyUpdatedEvent(@RequestBody CompanyUpdatedEventMessage message) {
        Optional.ofNullable(companyService.findById(message.getId(), true)).ifPresent(company->{
        	accountCrudService.updateAccountAfterCompanyIsUpdated(company);
        	contactCrudService.updateContactAfterCompanyIsUpdated(company);
        });
    }

}
