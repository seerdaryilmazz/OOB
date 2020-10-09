package ekol.crm.account.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.crm.account.domain.dto.kartoteksservice.Company;
import ekol.crm.account.domain.model.Account;
import ekol.crm.account.domain.model.Contact;
import ekol.crm.account.event.ContactUpdateEventProducer;
import ekol.crm.account.repository.ContactRepository;
import ekol.crm.account.validator.ContactValidator;
import ekol.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ContactCrudService {

    private ContactRepository repository;
    private ContactValidator validator;
    private AccountCrudService accountService;
    private ContactUpdateEventProducer contactUpdateEventProducer;

    @Transactional
    public Contact save(Long accountId, Contact contact){

        if(accountId != null){
            Account account = accountService.getByIdOrThrowException(accountId);
            contact.setAccount(account);
        }
        this.validator.validate(contact);
        if(contact.getId() != null){
            getByIdOrThrowException(contact.getId());
        }
        Contact savedContact = this.repository.save(contact);
        this.contactUpdateEventProducer.produceUpdate(savedContact);
        return savedContact;
    }

    public List<Contact> getByAccountId(Long accountId){
        return repository.findByAccountId(accountId);
    }

    public Contact getByIdOrThrowException(Long id) {
        return this.repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Contact with id {0} not found", id)
        );
    }

    public void delete(Contact contact){
        contact.setDeleted(true);
        Contact savedContact = this.repository.save(contact);
        this.contactUpdateEventProducer.produceDelete(savedContact);
    }
    
    @Transactional
    public void updateContactAfterCompanyIsUpdated(Company company) {
    	List<Contact> accountContacts = Optional.of(company)
    		.map(Company::getId)
    		.map(accountService::getByCompanyId)
    		.map(Account::getId)
    		.map(this::getByAccountId)
    		.orElseGet(ArrayList::new);
    	
        company.getCompanyContacts().forEach(contact->{
        	accountContacts.stream()
        		.filter(t->Objects.equals(t.getCompanyContactId(), contact.getId()))
        		.filter(t->!Objects.equals(t.getFullName(), contact.getFullName()))
        		.findFirst()
        		.ifPresent(t->{
        			t.setFirstName(contact.getFirstName());
        			t.setLastName(contact.getLastName());
        			save(t.getAccount().getId(), t);
        		});
        });
    }

}
