package ekol.crm.account.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.account.domain.dto.ContactJson;
import ekol.crm.account.domain.model.Contact;
import ekol.crm.account.repository.ContactRepository;
import ekol.crm.account.service.ContactCrudService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/contact")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ContactController {

    private ContactCrudService contactCrudService;
    private ContactRepository contactRepository;

    @PostMapping("/{accountId}")
    public ContactJson saveContact(@PathVariable Long accountId, @RequestBody ContactJson request) {
        request.validate();
        return ContactJson.fromEntity(contactCrudService.save(accountId, request.toEntity()));
    }

    @PostMapping("/multipleContacts/{accountId}")
    public List<ContactJson> saveMultipleContact(@PathVariable Long accountId, @RequestBody List<ContactJson> contactJsons) {
        List<ContactJson> addedContacts = new ArrayList<>();
        contactJsons.forEach(contactJson -> {
            contactJson.validate();
            addedContacts.add(ContactJson.fromEntity(contactCrudService.save(accountId, contactJson.toEntity())));
        });
        return addedContacts;
    }

    @GetMapping("/byAccount/{accountId}")
    public List<ContactJson> retrieveContacts(@PathVariable Long accountId) {
        return contactCrudService.getByAccountId(accountId).stream().map(ContactJson::fromEntity).collect(Collectors.toList());
    }

    @DeleteMapping("/{contactId}")
    public void deleteContact(@PathVariable Long contactId){
        Contact contact = contactCrudService.getByIdOrThrowException(contactId);
        contactCrudService.delete(contact);
    }

    /**
     * @param companyContactId kartoteks-service'teki CompanyContact'ın id'si
     */
    @GetMapping("/is-company-contact-linked-to-account")
    public boolean isCompanyContactLinkedToAccount(@RequestParam Long companyContactId, @RequestParam Long accountId) {
    	return 0 < contactRepository.countByCompanyContactIdAndAccountId(companyContactId, accountId);
    }
}
