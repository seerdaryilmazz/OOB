package ekol.crm.account.controller;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.constraints.Max;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ekol.crm.account.domain.dto.*;
import ekol.crm.account.domain.enumaration.*;
import ekol.crm.account.domain.model.Account;
import ekol.crm.account.repository.AccountRepository;
import ekol.crm.account.service.*;
import ekol.crm.account.validator.AccountValidator;
import ekol.event.auth.Authorize;
import ekol.exceptions.*;
import ekol.notification.annotation.SendNotification;
import ekol.resource.oauth2.SessionOwner;
import io.micrometer.core.annotation.Timed;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/account")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class AccountController {

	private SessionOwner sessionOwner;
    private AccountCrudService accountCrudService;
    private AccountAggregateCrudService accountAggregateCrudService;
    private AccountRepository accountRepository;
    private CompanyBlockageService companyBlockageService;
    private AccountMergeService accountMergeService;
    private AccountValidator accountValidator;
    private CompanyCrInfoService companyCrInfoService;
    
    private void checkAccountIsNull(Account account){
        if(account == null) {
            throw new BadRequestException("company is null");
        }
    }
    
    @Timed(value = "crm-account.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @PostMapping
    public AccountJson createAccount(@RequestBody AccountJson request) {
        request.setAccountType(AccountType.PROSPECT);
        request.validate();
        return AccountJson.fromEntity(accountCrudService.save(request.toEntity()));
    }

    @Authorize(operations= {"crm-account.update","crm-account.power-update"})
    @Timed(value = "crm-account.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @PutMapping("/{id}")
    @SendNotification(concern="ACCOUNT_OWNER_CHANGES", beforeCondition= "@accountNotificationCondition.isOwnerChanged(args[1])", target="result.accountOwner")
    public AccountJson updateAccount(@PathVariable Long id, @RequestBody AccountJson request) {
        request.setId(id);
        request.validate();
        return AccountJson.fromEntity(accountCrudService.save(request.toEntity()));
    }

    @PutMapping("/deleteMultipleGlobalAccountId/{id}")
    public void deleteMultipleGlobalAccountId(@PathVariable Long id) {
        List<Account> accounts = accountRepository.findAllByGlobalAccountId(id);
        accounts.forEach(account -> {
            account.getDetails().setGlobalAccountId(null);
            accountCrudService.save(account);
        });
    }
    
	@Authorize(operations = { "crm-account.power-update" })
    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id){
        accountCrudService.delete(id);
    }
	
	@Authorize(operations= {"crm-account.update"})
	@DeleteMapping("/{id}/by-owner")
	public void deleteAccountByOwner(@PathVariable Long id){
		Account account = accountCrudService.getByIdOrThrowException(id);
		if(!Objects.equals(sessionOwner.getCurrentUser().getUsername(), account.getAccountOwner())){
			throw new ValidationException("Account can be deleted by owner");
		}
		accountCrudService.delete(account);
	}

    @GetMapping("/check-if-company-has-account")
    public boolean checkIfCompanyHasAccount(@RequestParam Long companyId) {
        return accountCrudService.checkIfCompanyHasAccount(companyId);
    }

    @GetMapping
    public Page<AccountJson> listAccounts(@RequestParam @Max(100) Integer size, @RequestParam Integer page) {

        Pageable pageRequest = new PageRequest(page, size);

        Page<Account> entityPage = accountCrudService.list(pageRequest);

        List<AccountJson> accounts = entityPage.getContent().stream().map(AccountJson::fromEntity).collect(Collectors.toList());
        return new PageImpl<>(accounts, pageRequest, entityPage.getTotalElements());
    }

    @GetMapping("/detailed")
    public Page<AccountJson> listDetailedAccounts(@RequestParam @Max(100) Integer size, @RequestParam Integer page) {
        Pageable pageRequest = new PageRequest(page, size);
        return accountAggregateCrudService.listAccounts(pageRequest);
    }
    

    @RequestMapping(value="/validate-account",method = RequestMethod.PUT)
    public void validateAccount(@RequestBody Account account){
        checkAccountIsNull(account);
        accountValidator.validate(account);
    }


    @Timed(value = "crm-account.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping("/{accountId}")
    public AccountJson retrieveAccountById(@PathVariable Long accountId) {
        return AccountJson.fromEntity(accountCrudService.getByIdOrThrowException(accountId));
    }

    @Timed(value = "crm-account.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping("/byCompany")
    public AccountJson retrieveAccountByCompany(
            @RequestParam Long companyId,
            @RequestParam(required = false) Boolean throwExceptionIfNotFound) {

        Account account = null;
        if (Optional.ofNullable(throwExceptionIfNotFound).orElse(Boolean.TRUE)) {
            account = accountCrudService.getByCompanyIdOrThrowException(companyId);
        } else {
            account = accountCrudService.getByCompanyId(companyId);
        }
        return Optional.ofNullable(account).map(AccountJson::fromEntity).orElse(null);
    }

    @GetMapping("/{id}/detailed")
    public AccountJson retrieveDetailedAccount(@PathVariable Long id) {
        return accountAggregateCrudService.getAccountById(id);
    }

    @GetMapping("/byGlobalAccountId/{globalAccountId}")
    public Page<AccountJson> retrieveAccountsByGlobalAccountId(
            @PathVariable Long globalAccountId,
            @RequestParam @Max(100) Integer size,
            @RequestParam Integer page) {
        Page<Account> accountPage = accountAggregateCrudService.getAccountsByGlobalAccountId(
                globalAccountId,
                size,
                page);
        return accountAggregateCrudService.convertToAccountJsonPage(accountPage);
    }


    @GetMapping("/byAccountOwner")
    public List<AccountJson> retrieveAccountsByAccountOwner(@RequestParam String accountOwner) {
        return accountRepository.findByAccountOwner(accountOwner).stream().map(AccountJson::fromEntity).collect(Collectors.toList());
    }

    @PostMapping("/byAccountOwners")
    public List<AccountJson> retrieveAccountsByAccountOwners(@RequestBody List<String> accountOwners){
        return accountRepository.findByAccountOwnerIn(accountOwners).stream().map(AccountJson::fromEntity).collect(Collectors.toList());
    }

    @Timed(value = "crm-account.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping("/search")
    public Page<AccountJson> search(@RequestParam Integer page,
                                    @RequestParam(required = false, defaultValue = "20") @Max(100) Integer pageSize,
                                    @RequestParam(required = false) String countryCode,
                                    @RequestParam(required = false) Long id,
                                    @RequestParam(required = false) String name,
                                    @RequestParam(required = false) AccountType accountType,
                                    @RequestParam(required = false) SegmentType segmentType,
                                    @RequestParam(required = false) String accountOwner,
                                    @RequestParam(required = false) String parentSectorCode,
                                    @RequestParam(required = false) String subSectorCode,
                                    @RequestParam(required = false) String city,
                                    @RequestParam(required = false) String district,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate minCreationDate,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate maxCreationDate){
        AccountSearchCriteria searchCriteria = AccountSearchCriteria.builder()
                                                .countryCode(countryCode)
                                                .id(id)
                                                .name(name)
                                                .accountType(accountType)
                                                .segmentType(segmentType)
                                                .accountOwner(accountOwner)
                                                .parentSectorCode(parentSectorCode)
                                                .subSectorCode(subSectorCode)
                                                .minCreationDate(minCreationDate)
                                                .maxCreationDate(maxCreationDate).build();

        Page<Account> accountPage= accountCrudService.search(searchCriteria,city,district,page,pageSize);
        return accountCrudService.convertToAccountJsonPage(accountPage);
    }

    @Timed(value = "crm-account.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @PutMapping("/update-with-criteria")
    public void updateAccountWithCriteria(@RequestBody AccountChangeJson changeJson) {
        accountCrudService.updateByCriteria(changeJson);
    }
    
    @Timed(value = "crm-account.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping("/company-blockage")
    public BlockageType getCompanyBlockage(@RequestParam Long companyId) {
    	return companyBlockageService.getCompanyBlockage(companyId).getBlockageType();
    }
    
    @GetMapping("/cache/company-blockage")
    public void clearCompanyBlockageCache() {
    	companyBlockageService.clearCache();
    }
    
    @Timed(value = "crm-account.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @Authorize(operations = "crm-account.power-update")
    @PutMapping("/{accountId}/merge-with/{otherAccountId}")
    public Account saveMergedAccount(@PathVariable Long accountId, @PathVariable Long otherAccountId, @RequestBody AccountMergeJson accountMergeJson) {
        return accountMergeService.merge(accountId, otherAccountId, accountMergeJson);
    }

    @GetMapping("/company-cr-info")
    public List<CompanyCrInfo> getCompanyCrInfo(@RequestParam Long companyId){
        return companyCrInfoService.getCompanyCrInfo(companyId);
    }

}
