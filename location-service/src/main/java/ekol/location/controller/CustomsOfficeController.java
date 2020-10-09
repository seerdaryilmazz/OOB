package ekol.location.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.event.auth.Authorize;
import ekol.exceptions.ValidationException;
import ekol.location.domain.dto.SearchCustomsOfficeJson;
import ekol.location.domain.location.customs.CustomsOffice;
import ekol.location.domain.location.customs.CustomsOfficeContact;
import ekol.location.domain.location.customs.CustomsOfficeLocation;
import ekol.location.service.CustomsOfficeService;
import ekol.model.IdNamePair;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/location/customs-office")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class CustomsOfficeController {

    private CustomsOfficeService customsOfficeService;

    @Authorize(operations="location.customs-office.manage")
    @PostMapping(value = "")
    public CustomsOffice save(@RequestBody CustomsOffice customsOffice) {
        return customsOfficeService.create(customsOffice);
    }

    @Authorize(operations="location.customs-office.manage")
    @PutMapping(value = "/{id}")
    public CustomsOffice update(@PathVariable Long id, @RequestBody CustomsOffice customsOffice) {
        if(!customsOffice.getId().equals(id)){
            throw new ValidationException("Id mismatch");
        }
        return customsOfficeService.update(customsOffice);
    }

    @GetMapping(value = "")
    public Iterable<CustomsOffice> list() {
        return customsOfficeService.list();
    }

    @GetMapping(value = "/{id}")
    public CustomsOffice get(@PathVariable Long id) {
        return customsOfficeService.getWithDetailsOrThrowException(id);
    }

    @GetMapping(value = "/{id}/locations")
    public Set<CustomsOfficeLocation> getLocations(@PathVariable Long id) {
        return customsOfficeService.getCustomsOfficeLocations(id);
    }
    @GetMapping(value = "/{id}/contacts")
    public Set<CustomsOfficeContact> getContacts(@PathVariable Long id) {
        return customsOfficeService.getCustomsOfficeContacts(id);
    }
    
    @GetMapping(value = "/location/{id}/contacts")
    public Set<CustomsOfficeContact> getLocationContacts(@PathVariable Long id) {
    	return customsOfficeService.getCustomsOfficeLocationContacts(id);
    }

    @Authorize(operations="location.customs-office.manage")
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable Long id) {
        customsOfficeService.delete(id);
    }

    @GetMapping(value = "/border-customs")
    public List<IdNamePair> getBorderCustoms() {
        return customsOfficeService.getBorderCustoms().stream()
                .map(customsOffice -> new IdNamePair(customsOffice.getId(), customsOffice.getShortName()))
                .collect(Collectors.toList());
    }
    
    @PostMapping("/validate-name")
    public void validateCustomsOfficeNames(@RequestBody CustomsOffice customsOffice) {
    	customsOfficeService.validateNameExisting(customsOffice);
    }
    
    @PostMapping("/search")
    public Page<CustomsOffice> searchWarehouse(@RequestBody SearchCustomsOfficeJson terms) {
    	return customsOfficeService.searchCustomsOffice(terms);
    }
    
    @GetMapping("/location/{id}")
    public CustomsOfficeLocation getCustomsOfficeLocation(@PathVariable Long id) {
    	return customsOfficeService.getCustomsOfficeLocation(id);
    }
}
