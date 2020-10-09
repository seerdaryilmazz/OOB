package ekol.crm.opportunity.validation;

import java.util.*;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ekol.crm.opportunity.domain.dto.*;
import ekol.crm.opportunity.domain.enumaration.*;
import ekol.crm.opportunity.service.CrmQuoteService;
import ekol.exceptions.ValidationException;
import ekol.model.CodeNamePair;
import ekol.resource.validation.Validator;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CancelOpportunityValidation implements Validator {

	private CrmQuoteService quoteService;
	
	@Override
	public void validate(Object[] args) {
		OpportunityJson opportunity = Stream.of(args).filter(OpportunityJson.class::isInstance).map(OpportunityJson.class::cast).findFirst().orElse(null);
		if(Objects.isNull(opportunity) ||  Objects.isNull(opportunity.getId())) {
			return;
		}
		if(OpportunityStatus.CANCELED != opportunity.getStatus()) {
			return;
		}
		
		List<QuoteJson> quotes = quoteService.getQuotesCreatedFromOpportunity(opportunity.getId(), true);
		if(quotes.stream().map(QuoteJson::getStatus).map(CodeNamePair::getCode).anyMatch(status->!Objects.equals("CANCELED", status))) {
			throw new ValidationException("Since the opportunity has related quotes, it cannot be canceled. Please cancel the quotes first");
		}
	}
}
