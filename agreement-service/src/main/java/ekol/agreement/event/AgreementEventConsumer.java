package ekol.agreement.event;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.agreement.domain.model.agreement.Agreement;
import ekol.agreement.service.AgreementService;
import ekol.event.annotation.ConsumesWebEvent;
import ekol.model.IdNamePair;

@RestController
@RequestMapping("/event-consumer")
public class AgreementEventConsumer {
	
	@Autowired
	private AgreementService agreementService;
	
    @ConsumesWebEvent(event= "account-merge", name= "consume agreements in account merge")
    @PostMapping("/account-merge")
    public void consumeMergeAccountEvent(@RequestBody AccountMerge accountMerge) {
 
    	Set<Long> agreementIds = accountMerge.getAgreements().stream().map(IdNamePair::getId).collect(Collectors.toSet());
    	Iterable<Agreement> agreements = agreementService.listAll(agreementIds);
    	agreements.forEach(agreement -> {
    		agreement.setAccount(accountMerge.getAccount());
    		agreementService.save(agreement.toJson());
    	});
    }

}
