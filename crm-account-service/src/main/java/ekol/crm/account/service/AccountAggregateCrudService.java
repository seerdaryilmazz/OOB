package ekol.crm.account.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import ekol.crm.account.domain.dto.*;
import ekol.crm.account.domain.model.*;
import ekol.crm.account.domain.model.potential.Potential;
import ekol.crm.account.repository.AccountRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AccountAggregateCrudService {

    private AccountCrudService accountCrudService;
    private PotentialCrudService potentialCrudService;
    private ContactCrudService contactCrudService;
    private AccountRepository accountRepository;
    private CompanyService companyService;

    public Page<AccountJson> listAccounts(Pageable pageRequest){

        Page<Account> entityPage = accountCrudService.list(pageRequest);

        List<AccountJson> accounts = entityPage.getContent().stream().map((entity) -> {
            AccountJson account = AccountJson.fromEntity(entity);
            fillAccount(account);
            return account;
        }).collect(Collectors.toList());

        return new PageImpl<>(accounts, pageRequest, entityPage.getTotalElements());
    }

    public AccountJson getAccountById(Long id){
        AccountJson account = AccountJson.fromEntity(accountCrudService.getByIdOrThrowException(id));
        fillAccount(account);
        return account;
    }
    
    public Page<Account> getAccountsByGlobalAccountId(Long globalAccountId, Integer size, Integer page){
        return accountRepository.findByGlobalAccountId(globalAccountId, new PageRequest(page, size,Sort.Direction.ASC, "name"));
    }

    private void fillAccount(AccountJson account){
        List<Potential> potentials = potentialCrudService.getByAccountId(account.getId());
        account.setPotentials(potentials.stream().map(Potential::toJson).collect(Collectors.toList()));

        List<Contact> contacts = contactCrudService.getByAccountId(account.getId());
        account.setContacts(contacts.stream().map(ContactJson::fromEntity).collect(Collectors.toList()));
    }

    public Page<AccountJson> convertToAccountJsonPage(Page<Account> accountPage) {
        List<AccountJson> accountJsons = accountPage.getContent().stream().map(AccountJson::fromEntity).collect(Collectors.toList());
        return new PageImpl<>(accountJsons, new PageRequest(accountPage.getNumber(), accountPage.getSize()), accountPage.getTotalElements());
    }

    public List<Long> findCompanyIdsByCityAndDistrict(String city, String district) {
        List<Long> ids = new ArrayList<>();
        CompanySearchIterator iterator = new CompanySearchIterator(companyService, city, district, true, 1000);
        while (iterator.hasNext()) {
            ids.add(iterator.next());
        }
        return ids;
    }
}
