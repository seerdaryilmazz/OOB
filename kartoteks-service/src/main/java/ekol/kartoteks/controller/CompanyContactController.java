package ekol.kartoteks.controller;


import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.event.auth.Authorize;
import ekol.exceptions.*;
import ekol.kartoteks.domain.CompanyContact;
import ekol.kartoteks.domain.dto.BulkExistenceAndActivenessCheckResponse;
import ekol.kartoteks.domain.validator.CompanyContactValidator;
import ekol.kartoteks.repository.CompanyContactRepository;
import ekol.kartoteks.utils.ServiceUtils;


/**
 * Created by fatmaozyildirim on 3/15/16.
 */
@RestController
@RequestMapping("/contact")
public class CompanyContactController {
    @Autowired
    private CompanyContactRepository companyContactRepository;
    @Autowired
    private CompanyContactValidator companyContactValidator;

    @Authorize(operations = "kartoteks.company.delete-company")
    @DeleteMapping("/{contactId}")
    public void delete( @PathVariable Long contactId){
        CompanyContact c = Optional.ofNullable(companyContactRepository.findOne(contactId)).orElseThrow(ResourceNotFoundException::new);
        c.setDeleted(true);
        companyContactRepository.save(c);
    }

    @Authorize(operations = {"kartoteks.company.edit-company", "kartoteks.company.edit-temp-company"})
    @PutMapping("/{contactId}")
    public CompanyContact update(@PathVariable Long contactId, @RequestBody CompanyContact companyContact){
        CompanyContact c= Optional.ofNullable(companyContactRepository.findOne(contactId)).orElseThrow(ResourceNotFoundException::new);

        if(companyContact == null || companyContact.getId() == null)
            throw new BadRequestException("Company Contact is null");

        if(companyContact.getId().longValue() != c.getId().longValue())
            throw new BadRequestException("Company Contact ids are not equal");

        companyContactRepository.save(companyContact);
        return companyContact;
    }

    @GetMapping("/{contactId}")
    public CompanyContact get(@PathVariable Long contactId){
    	return Optional.ofNullable(companyContactRepository.findOne(contactId)).orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping("/contactIds/{contactIds}")
    public List<CompanyContact> getContacts(@PathVariable List<Long> contactIds){
        return Optional.ofNullable(companyContactRepository.findByCompanyIds(contactIds)).orElseThrow(ResourceNotFoundException::new);
    }

    @Authorize(operations = {"kartoteks.company.edit-company", "kartoteks.company.edit-temp-company"})
	@PostMapping({ "/", "" })
    public CompanyContact add(@RequestBody CompanyContact companyContact){
        if(companyContact == null)
            throw new BadRequestException("Company contact is null");

        companyContactRepository.save(companyContact);
        return companyContact;

    }

    @Authorize(operations = {"kartoteks.company.edit-company", "kartoteks.company.edit-temp-company", "kartoteks.company.create-company"})
    @PutMapping("/validate")
    public void validateContact(@RequestBody CompanyContact contact){
        if(contact == null) {
            throw new BadRequestException("CompanyContact is null");
        }
        companyContactValidator.validate(contact);
    }

    @GetMapping("/{contactId}/is-linked-to-location")
    public boolean isLinkedToLocation(@PathVariable Long contactId, @RequestParam Long locationId) {
        return 0 < companyContactRepository.countByIdAndCompanyLocationId(contactId, locationId);
    }

    @GetMapping("/{contactId}/is-active")
    public boolean isActive(@PathVariable Long contactId) {
        return 0 < companyContactRepository.countByIdAndActiveTrue(contactId);
    }

    @GetMapping("/bulk-existence-and-activeness-check")
    public BulkExistenceAndActivenessCheckResponse doBulkExistenceAndActivenessCheck(@RequestParam String commaSeparatedIds) {

        List<Long> ids = ServiceUtils.commaSeparatedNumbersToLongList(commaSeparatedIds);
        Set<Long> ok = new HashSet<>(companyContactRepository.findIdsByActiveTrueAndIdIn(ids));
        Set<Long> notOk = new HashSet<>();

        for (Long id : ids) {
            if (!ok.contains(id)) {
                notOk.add(id);
            }
        }

        return new BulkExistenceAndActivenessCheckResponse(ok, notOk);
    }
    
    @GetMapping("/list-by-email")
    public List<CompanyContact> listContactByEmail(@RequestParam String email){
    	return companyContactRepository.findByEmailsEmailEmailAddress(email);
    }
}
