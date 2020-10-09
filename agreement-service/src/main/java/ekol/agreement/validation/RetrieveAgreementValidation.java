package ekol.agreement.validation;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import ekol.agreement.client.AuthorizationServiceClient;
import ekol.agreement.domain.dto.Account;
import ekol.agreement.domain.model.OwnerInfo;
import ekol.agreement.domain.model.agreement.Agreement;
import ekol.agreement.service.*;
import ekol.exceptions.ValidationException;
import ekol.model.*;
import ekol.resource.oauth2.SessionOwner;
import ekol.resource.validation.Validator;

@Component
public class RetrieveAgreementValidation implements Validator {

	@Autowired
	private SessionOwner sessionOwner;
	
	@Autowired
	private AuthorizationServiceClient authorizationServiceClient;

	@Autowired
	private AgreementService agreementService;
	
	@Autowired
	private AccountService accountService;

	@Value("${spring.profiles.active}")
	private String[] activeProfile;

	@Override
	public void validate(Object[] args) {
		if(authorizationServiceClient.isAccess("agreement.view")) {
			return;
		}
		Agreement agreement = agreementService.getByIdOrThrowException(Long.class.cast(args[0]));
		if(agreement.getOwnerInfos().stream().map(OwnerInfo::getName).map(IdNamePair::getId).anyMatch(Long.valueOf(sessionOwner.getCurrentUser().getId())::equals)) {
			return;
		}
		Account account  = accountService.findAccountById(agreement.getAccount().getId(), true);
		if(null == account) {
			throw new ValidationException("Agreement account not found");
		}
		if(Objects.equals(account.getAccountOwner(), sessionOwner.getCurrentUser().getUsername())) {
			return;
		}
		List<User> managers = authorizationServiceClient.listUserManagers(account.getAccountOwner());
		if(managers.stream().map(User::getUsername).anyMatch(sessionOwner.getCurrentUser().getUsername()::equals)) {
			return;
		}
		throw new ValidationException("You are not authorized to view this agreement");
	}
}
