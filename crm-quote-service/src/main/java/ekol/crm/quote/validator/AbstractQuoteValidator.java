package ekol.crm.quote.validator;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import ekol.crm.quote.client.*;
import ekol.crm.quote.domain.dto.accountservice.Account;
import ekol.crm.quote.domain.dto.kartoteksservice.*;
import ekol.crm.quote.domain.model.quote.Quote;
import ekol.crm.quote.service.*;
import ekol.exceptions.ValidationException;
import lombok.*;

@Getter(AccessLevel.PROTECTED)
@Component
public abstract class AbstractQuoteValidator {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
    private KartoteksService kartoteksService;
	
	protected abstract void validateQuote(Quote quote);
	
	protected abstract Map<Long, String> extractCompaniesId(Quote quote);
	
	public void validate(Quote quote){
		if(quote.getAccount() == null || quote.getAccount().getId() == null){
            throw new ValidationException("Quote should have a account");
        }
        if(quote.getAccountLocation() == null || quote.getAccountLocation().getId() == null){
            throw new ValidationException("Quote should have a account location");
        }
        if(quote.getSubsidiary() == null || quote.getSubsidiary().getId() == null){
            throw new ValidationException("Quote should have a subsidiary");
        }
        if(quote.getServiceArea() == null){
            throw new ValidationException("Service area should not be empty");
        }
        if(quote.getType() == null){
            throw new ValidationException("Type should not be empty");
        }
        if (quote.getStatus() == null) {
            throw new ValidationException("Status should not be empty");
        }
        
		validateQuote(quote);
		ensureLocationBelongToCompany(quote);
		ensureContactAndLocationsAreValid(quote);
	}
	
	protected void ensureLocationBelongToCompany(Quote quote) {
		Account account = Optional.ofNullable(accountService.findAccountById(quote.getAccount().getId(), true))
				.orElseThrow(()->new ValidationException("{0} named account is not exists", quote.getAccount().getName()));
		
		Company company = Optional.ofNullable(kartoteksService.findCompanyById(account.getCompany().getId(), true))
				.orElseThrow(()->new ValidationException("{0} named account company is not exists", account.getCompany().getName()));
		
		if(company.getCompanyLocations().parallelStream().map(CompanyLocation::getId).noneMatch(quote.getAccountLocation().getId()::equals)) {
			throw new ValidationException("{0} named account location does not belong to account company", quote.getAccountLocation().getName());
		}
	}
	
	public void ensureContactAndLocationsAreValid(Quote quote) {
		ensureLocationBelongToCompany(quote);
		Map<Long, String> companiesId = extractCompaniesId(quote);
		if(CollectionUtils.isEmpty(companiesId)) {
			return;
		}
        BulkExistenceAndActivenessCheckResponse response = kartoteksService.doBulkExistenceAndActivenessCheckForLocations(companiesId.keySet().parallelStream().collect(Collectors.toList()));
        response.getNotOk().forEach(id->{
        	throw new ValidationException("Selected {0} location is deleted or not active. Please select another one.", companiesId.get(id));
        });
    }
}
