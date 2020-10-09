package ekol.crm.account.service;

import java.util.*;
import java.util.stream.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.crm.account.domain.dto.*;
import ekol.crm.account.domain.dto.kartoteksservice.*;
import ekol.crm.account.domain.dto.potential.PotentialStatus;
import ekol.crm.account.domain.enumaration.AccountType;
import ekol.crm.account.domain.model.*;
import ekol.crm.account.domain.model.potential.Potential;
import ekol.crm.account.event.dto.*;
import ekol.crm.account.repository.*;
import ekol.crm.account.repository.specs.AccountSpecification;
import ekol.crm.account.validator.AccountValidator;
import ekol.exceptions.*;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AccountCrudService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountCrudService.class);

    private AccountRepository repository;
    private AccountValidator validator;
    private ApplicationEventPublisher applicationEventPublisher;
    private CompanyService companyService;
    private PotentialRepository potentialRepository;
    private ContactRepository contactRepository;
    private CrmSearchServiceClient crmSearchServiceClient;
    private Environment environment;
    
    public Page<Account> list(Pageable pageRequest) {
        return repository.findAll(pageRequest);
    }
    
    private Account saveAccount(Account account) {
    	if (Optional.ofNullable(account.getId()).isPresent()) {
    		repository.findById(account.getId()).ifPresent(existingAccount->{
    			if(!existingAccount.getAccountOwner().equalsIgnoreCase(account.getAccountOwner())) {
    				applicationEventPublisher.publishEvent(AccountEventMessage.with(account, Operation.ACCOUNT_OWNER_CHANGED));
    			}
    		});
    	} else {
        	applicationEventPublisher.publishEvent(AccountEventMessage.with(account, Operation.ACCOUNT_OWNER_CHANGED));
        }
    	Account savedAccount = repository.save(account);
    	applicationEventPublisher.publishEvent(savedAccount);
    	return savedAccount;
    }
    
    @Transactional
    public Account updateAccountType(Account account, AccountType accountType) {
    	account.setAccountType(accountType);
    	return saveAccount(account);
    }

    @Transactional
    public Account save(Account account){
        validator.validate(account);
        return saveAccount(account);
    }
    
    @Transactional
    public void delete(Long id){
    	delete(getByIdOrThrowException(id));
    }
    
    @Transactional
	public void delete(Account account){
		final String CANCELED = "CANCELED";
    	if(potentialRepository.findByAccountId(account.getId()).parallelStream().map(Potential::getStatus).anyMatch(PotentialStatus.ACTIVE::equals)) {
    		throw new ValidationException("The account that has active potentials cannot be deleted!");
    	}
    	if(CollectionUtils.isNotEmpty(contactRepository.findByAccountId(account.getId()))) {
    		throw new ValidationException("The account that has crm contacts cannot be deleted!");
    	} 
    	if(0 < crmSearchServiceClient.searchByAccount(account.getId(), CANCELED, "quote").getTotalElements()) {
    		throw new ValidationException("The account that has active quotes cannot be deleted!");
    	}
    	if(0 < crmSearchServiceClient.searchByAccount(account.getId(), CANCELED, "agreement").getTotalElements()) {
    		throw new ValidationException("The account that has active agreements cannot be deleted!");
    	}
    	if(0 < crmSearchServiceClient.searchByAccount(account.getId(), CANCELED, "opportunity").getTotalElements()) {
    		throw new ValidationException("The account that has active opportunities cannot be deleted!");
    	}
    	if(0 < crmSearchServiceClient.searchActivitiesByAccount(account.getId()).getTotalElements()) {
    		throw new ValidationException("The account that has active activities cannot be deleted!");
    	}
    	forceDelete(account);
    }
    @Transactional
   	public void forceDelete(Account account){
    	account.setDeleted(true);
    	applicationEventPublisher.publishEvent(AccountEventMessage.with(account, Operation.ACCOUNT_DELETE));
    	this.repository.save(account);
    }

    @Transactional
    public void updateAccountAfterCompanyIsUpdated(Company company) {
        
    	if(LOGGER.isWarnEnabled()) {
    		LOGGER.warn("consuming company-updated event: companyId: {}, companyName: {}", company.getId(), company.getName());
    	}

        Optional<Account> accountOptional = repository.findByCompanyId(company.getId());

        if (accountOptional.isPresent()) {

            boolean isAccountChanging = false;
            Account account = accountOptional.get();

            if (!account.getName().equals(company.getName()) || !account.getCompany().getName().equals(company.getName())) {
                account.setName(company.getName());
                account.getCompany().setName(company.getName());
                isAccountChanging = true;
            }

            CompanySector defaultCompanySector = company.getDefaultCompanySector();

            if (defaultCompanySector != null) {

                Sector sector = defaultCompanySector.getSector();

                if (sector.getParent() != null) {

                    CodeNamePair newSubSector = new CodeNamePair(sector.getCode(), sector.getName());

                    if (!newSubSector.equals(account.getSubSector())) {
                        account.setSubSector(newSubSector);
                        isAccountChanging = true;
                    }

                    CodeNamePair newParentSector = new CodeNamePair(sector.getParent().getCode(), sector.getParent().getName());

                    if (!newParentSector.equals(account.getParentSector())) {
                        account.setParentSector(newParentSector);
                        isAccountChanging = true;
                    }
                }
            }

            if (isAccountChanging) {
                saveAccount(account);
            }
        }
    }

    public boolean checkIfCompanyHasAccount(Long companyId) {
        return validator.checkIfCompanyHasAccount(companyId);
    }

    public Account getByIdOrThrowException(Long id) {
        if(id == null){
            throw new ValidationException("Account id should be sent");
        }
        return repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Account with id {0} not found", id)
        );
    }


    public Account getByCompanyIdOrThrowException(Long companyId) {
        if(companyId == null){
            throw new ValidationException("Company id should be sent");
        }
        return repository.findByCompanyId(companyId).orElseThrow(() ->
                new ResourceNotFoundException("Account with company id {0} not found", companyId)
        );
    }

    public Account getByCompanyId(Long companyId) {
        if (companyId == null) {
            throw new ValidationException("Company id should be sent");
        }
        return repository.findByCompanyId(companyId).orElse(null);
    }

    public Page<Account> search(AccountSearchCriteria searchCriteria, String city, String district,Integer page,Integer pageSize){
        Specifications<Account> specs = null;
        if(!StringUtils.isEmpty(searchCriteria.getCountryCode())){
            specs = AccountSpecification.append(specs, AccountSpecification.havingCountryCode(searchCriteria.getCountryCode()));
        }
        if(!StringUtils.isEmpty(searchCriteria.getName())){
            specs = AccountSpecification.append(specs, AccountSpecification.likeAccountName(searchCriteria.getName()));
        }
        if(searchCriteria.getId()!=null){
            specs = AccountSpecification.append(specs,AccountSpecification.havingId(searchCriteria.getId()));
        }
        if(searchCriteria.getAccountType() != null){
            specs = AccountSpecification.append(specs, AccountSpecification.havingAccountType(searchCriteria.getAccountType()));
        }
        if(searchCriteria.getSegmentType() != null){
            specs = AccountSpecification.append(specs, AccountSpecification.havingSegmentType(searchCriteria.getSegmentType()));
        }
        if(!StringUtils.isEmpty(searchCriteria.getAccountOwner())){
            specs = AccountSpecification.append(specs, AccountSpecification.havingAccountOwner(searchCriteria.getAccountOwner()));
        }
        if(!StringUtils.isEmpty(searchCriteria.getParentSectorCode())){
            specs = AccountSpecification.append(specs, AccountSpecification.havingParentSector(searchCriteria.getParentSectorCode()));
        }
        if(!StringUtils.isEmpty(searchCriteria.getSubSectorCode())){
            specs = AccountSpecification.append(specs, AccountSpecification.havingSubSector(searchCriteria.getSubSectorCode()));
        }
        if(searchCriteria.getMinCreationDate() != null){
            specs = AccountSpecification.append(specs, AccountSpecification.createdAtGreaterThanOrEqual(searchCriteria.getMinCreationDate().atStartOfDay()));
        }
        if(searchCriteria.getMaxCreationDate() != null){
            specs = AccountSpecification.append(specs, AccountSpecification.createdAtLessThanOrEqual(searchCriteria.getMaxCreationDate().plusDays(1).atStartOfDay()));
        }
        Pageable pageable = new PageRequest(page, pageSize, Sort.Direction.ASC, "name");
        if(!StringUtils.isBlank(city) || !StringUtils.isBlank(district)){
            List<Long> companyIds = findCompanyIdsByCityAndDistrict(city, district);
            if(CollectionUtils.isEmpty(companyIds)){
                return new CustomPageImpl<>(Collections.emptyList(), pageable, 0);
            }else{
                specs= AccountSpecification.append(specs,AccountSpecification.havingCompanyId(companyIds));
            }
        }

        return repository.findAll(specs,pageable);
    }

    public List<Long> findCompanyIdsByCityAndDistrict(String city, String district) {
        List<Long> ids = new ArrayList<>();
        CompanySearchIterator iterator = new CompanySearchIterator(companyService, city, district, true, 1000);
        while (iterator.hasNext()) {
            ids.add(iterator.next());
        }
        return ids;
    }

    public Page<AccountJson> convertToAccountJsonPage(Page<Account> accountPage) {
        List<AccountJson> accountJsons = accountPage.getContent().stream().map(AccountJson::fromEntity).collect(Collectors.toList());
        return new PageImpl<>(accountJsons, new PageRequest(accountPage.getNumber(), accountPage.getSize()), accountPage.getTotalElements());
    }

    @Transactional
    public void updateByCriteria(AccountChangeJson changeJson){
        if(changeJson == null || !changeJson.fieldToChangeExists()){
            throw new ValidationException("At least one field should be changed");
        }
        
        if(CollectionUtils.isNotEmpty(changeJson.getAccountIds())) {
        	repository.findAll(changeJson.getAccountIds()).forEach(account->{
        		if (Optional.ofNullable(changeJson.getAccountOwner()).isPresent()) {
                    applicationEventPublisher.publishEvent(AccountEventMessage.with(account, Operation.ACCOUNT_OWNER_CHANGED));
                }
        		changeJson.apply(account);
        		this.save(account);
        	});
        }
    }
}



