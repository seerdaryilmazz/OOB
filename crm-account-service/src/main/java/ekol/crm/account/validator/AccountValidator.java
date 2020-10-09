package ekol.crm.account.validator;

import ekol.crm.account.domain.model.Account;
import ekol.crm.account.repository.AccountRepository;
import ekol.exceptions.ValidationException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AccountValidator {

    private AccountRepository accountRepository;

    public void validate(Account account){
        if(StringUtils.isBlank(account.getName())){
            throw new ValidationException("Account should have a name");
        }
        if(account.getCompany() == null || account.getCompany().getId() == null){
            throw new ValidationException("Company should be assigned to an account");
        }
        if(StringUtils.isBlank(account.getCompany().getName())){
            throw new ValidationException("Account should have a company name");
        }
        if(StringUtils.isBlank(account.getAccountOwner())){
            throw new ValidationException("Account should have a account owner");
        }
        if(account.getAccountType() == null){
            throw new ValidationException("Account should have a account type");
        }
        if(account.getSegment() == null){
            throw new ValidationException("Account should have a segment");
        }
        if(account.getParentSector() == null || StringUtils.isEmpty(account.getParentSector().getCode())){
            throw new ValidationException("Account should have a parent sector");
        }
        if(account.getSubSector() == null || StringUtils.isEmpty(account.getSubSector().getCode())){
            throw new ValidationException("Account should have a sub sector");
        }
        if(account.getCountry() == null || StringUtils.isBlank(account.getCountry().getIso())){
            throw new ValidationException("Account should have a company's country code");
        }
        if(account.getId() == null){
            if (checkIfCompanyHasAccount(account.getCompany().getId())) {
                throw new ValidationException("This company already has an account");
            }
        }
    }

    public boolean checkIfCompanyHasAccount(Long companyId){
        return accountRepository.findByCompanyId(companyId).isPresent();
    }


}
