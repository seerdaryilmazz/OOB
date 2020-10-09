package ekol.crm.opportunity.validation;

import java.util.Objects;
import java.util.function.Supplier;
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
public class CloseOpportunityValidation implements Validator {

	private CrmQuoteService qouteService;
	
	@Override
	public void validate(Object[] args) {
		OpportunityJson opportunity = Stream.of(args).filter(OpportunityJson.class::isInstance).map(OpportunityJson.class::cast).findFirst().orElse(null);
		if(Objects.isNull(opportunity) ||  Objects.isNull(opportunity.getId())) {
			return;
		}
		
		if(Stream.of(OpportunityStatus.REJECTED, OpportunityStatus.WITHDRAWN, OpportunityStatus.CLOSED).noneMatch(opportunity.getStatus()::equals)) {
			return;
		}

		if(Objects.isNull(opportunity.getCloseReason())) {
			throw new ValidationException("Close Reason must be specified");
		}
		
		if(Stream.of(CloseType.REJECT, CloseType.WITHDRAWN, CloseType.CLOSE).noneMatch(opportunity.getCloseReason().getType()::equals)) {
			return;
		}
		
		Supplier<Stream<String>> quoteStatusStream = ()->qouteService.getQuotesCreatedFromOpportunity(opportunity.getId(), true).stream().map(QuoteJson::getStatus).map(CodeNamePair::getCode);
		if(quoteStatusStream.get().anyMatch(status->!Objects.equals("CANCELED", status))) {
			if(CloseType.REJECT == opportunity.getCloseReason().getType()) {
				throw new ValidationException("Since the opportunity has related quotes, it cannot be rejected. Please cancel the quotes first");
			}
			if(CloseType.WITHDRAWN == opportunity.getCloseReason().getType()) {
				throw new ValidationException("Since the opportunity has related quotes, it cannot be withdrawn. Please cancel the quotes first");
			}
		}
		if(CloseType.CLOSE == opportunity.getCloseReason().getType()) {
			if(quoteStatusStream.get().anyMatch(status -> Stream.of("OPEN", "PDF_CREATED").anyMatch(status::equals))){
				throw new ValidationException("Please close all related quotes before closing opportunity!");
			}
			if(quoteStatusStream.get().allMatch("CANCELED"::equals)){
				throw new ValidationException("Opportunities with no related quotes can't be closed!");
			}
		}
	}

}
