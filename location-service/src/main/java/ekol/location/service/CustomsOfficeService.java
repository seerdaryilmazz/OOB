package ekol.location.service;

import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.exceptions.*;
import ekol.location.domain.Country;
import ekol.location.domain.dto.SearchCustomsOfficeJson;
import ekol.location.domain.location.customs.*;
import ekol.location.event.*;
import ekol.location.repository.*;
import ekol.location.validator.CustomsOfficeValidator;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class CustomsOfficeService {

    private CustomsOfficeRepository customsOfficeRepository;
    private CustomsOfficeLocationRepository customsOfficeLocationRepository;
    private CustomsOfficeContactRepository customsOfficeContactRepository;
    private CountryRepository countryRepository;
    private CustomsOfficeValidator validator;
    private ApplicationEventPublisher publisher;

    @Transactional
    public CustomsOffice create(CustomsOffice customsOffice) {
        if(StringUtils.isBlank(customsOffice.getLocalName())){
            customsOffice.setLocalName(customsOffice.getName());
        }
        fillLocationLocalNames(customsOffice);
        validator.validateNew(customsOffice);

        Country country = countryRepository.findByIso(customsOffice.getCountry().getIso());
        if (country == null) {
            throw new ValidationException(
                    "Country with code '{0}' does not exist.", customsOffice.getCountry().getIso());
        }
        customsOffice.setCountry(country);
        CustomsOffice savedCustomsOffice = customsOfficeRepository.save(customsOffice);
        savedCustomsOffice.setLocations(saveLocations(customsOffice, savedCustomsOffice));
        customsOffice.getContacts().forEach(customsOfficeContact -> {
            Optional<CustomsOfficeLocation> contactLocation = savedCustomsOffice.getLocations().stream().filter(location ->
                    location.getName().equals(customsOfficeContact.getCustomsLocation().getName())).findFirst();
            customsOfficeContact.setCustomsLocation(contactLocation.orElse(null));
        });
        savedCustomsOffice.setContacts(saveContacts(customsOffice, savedCustomsOffice));
        publisher.publishEvent(EventMessageWrapper.with(savedCustomsOffice, Operation.CREATE));
        return savedCustomsOffice;
    }

    private void fillLocationLocalNames(CustomsOffice customsOffice){
        customsOffice.getLocations().forEach(location -> {
            if(StringUtils.isBlank(location.getLocalName())){
                location.setLocalName(location.getName());
            }
        });
    }

    private Set<CustomsOfficeLocation> saveLocations(CustomsOffice customsOffice, CustomsOffice savedCustomsOffice){
        Set<CustomsOfficeLocation> savedLocations = new HashSet<>();
        customsOffice.getLocations().forEach(customsOfficeLocation -> {
            Country country = countryRepository.findByIso(customsOfficeLocation.getCountry().getIso());
            if (country == null) {
                throw new ValidationException(
                        "Country with code '{0}' does not exist.", customsOffice.getCountry().getIso());
            }
            customsOfficeLocation.setCountry(country);
            customsOfficeLocation.setTimezone(country.getTimezone());
            customsOfficeLocation.setCustomsOffice(savedCustomsOffice);
            savedLocations.add(save(customsOfficeLocation));
        });
        return savedLocations;
    }

    private Set<CustomsOfficeContact> saveContacts(CustomsOffice customsOffice, CustomsOffice savedCustomsOffice){
        Set<CustomsOfficeContact> savedContacts = new HashSet<>();
        customsOffice.getContacts().forEach(customsOfficeContact -> {
            customsOfficeContact.setCustomsOffice(savedCustomsOffice);
            savedContacts.add(save(customsOfficeContact));
        });
        return savedContacts;
    }

    @Transactional
    public CustomsOffice update(CustomsOffice customsOffice) {
        CustomsOffice savedCustomsOffice = getOrThrowException(customsOffice.getId());
        fillLocationLocalNames(customsOffice);
        validator.validate(customsOffice);
        customsOffice.setLocations(saveLocations(customsOffice, savedCustomsOffice));
        customsOffice.getContacts().forEach(customsOfficeContact -> {
            Optional<CustomsOfficeLocation> contactLocation = customsOffice.getLocations().stream().filter(location -> 
            Objects.nonNull(customsOfficeContact.getCustomsLocation()) && ((Objects.nonNull(customsOfficeContact.getCustomsLocation().getId()) && Objects.equals(location.getId(), customsOfficeContact.getCustomsLocation().getId()))
             || Objects.equals(location.getName(), customsOfficeContact.getCustomsLocation().getName()))).findFirst();
            customsOfficeContact.setCustomsLocation(contactLocation.orElse(null));
        });
        customsOffice.setContacts(saveContacts(customsOffice, savedCustomsOffice));

        Country country = countryRepository.findByIso(customsOffice.getCountry().getIso());
        if (country == null) {
            throw new ValidationException(
                    "Country with code '{0}' does not exist.", customsOffice.getCountry().getIso());
        }
        customsOffice.setCountry(country);
        savedCustomsOffice = customsOfficeRepository.save(customsOffice);
        publisher.publishEvent(EventMessageWrapper.with(savedCustomsOffice, Operation.UPDATE));
        return savedCustomsOffice;
    }

    private CustomsOfficeLocation save(CustomsOfficeLocation location){
        Country country = countryRepository.findByIso(location.getCountry().getIso());
        if (country == null) {
            throw new ValidationException(
                    "Country with code '{0}' does not exist.", location.getCountry().getIso());
        }
        location.setCountry(country);
        return customsOfficeLocationRepository.save(location);
    }

    private CustomsOfficeContact save(CustomsOfficeContact contact){
        return customsOfficeContactRepository.save(contact);
    }

    public Iterable<CustomsOffice> list() {
        return customsOfficeRepository.findAll();
    }

    public CustomsOffice getOrThrowException(Long id){
        return customsOfficeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Customs office with id {0} not found", id.toString()));
    }

    public CustomsOffice getWithDetailsOrThrowException(Long id){
        return customsOfficeRepository.findWithLocationsAndContactsById(id).orElseThrow(() ->
                new ResourceNotFoundException("Customs office with id {0} not found", id.toString()));
    }

    public Set<CustomsOfficeLocation> getCustomsOfficeLocations(Long id){
        CustomsOffice office = customsOfficeRepository.findWithLocationsAndContactsById(id).orElseThrow(() ->
                new ResourceNotFoundException("Customs office with id {0} not found", id.toString()));
        return office.getLocations();
    }

    public Set<CustomsOfficeContact> getCustomsOfficeLocationContacts(Long locationId){
    	return Optional.of(getCustomsOfficeLocation(locationId))
    			.map(CustomsOfficeLocation::getCustomsOffice)
    			.map(CustomsOffice::getContacts)
    			.map(Collection::stream)
    			.orElseGet(Stream::empty)
    			.filter(t->Objects.nonNull(t.getCustomsLocation()))
    			.filter(t->Objects.equals(locationId, t.getCustomsLocation().getId()))
    			.collect(Collectors.toSet());
    }
    
    public Set<CustomsOfficeContact> getCustomsOfficeContacts(Long id){
        CustomsOffice office = customsOfficeRepository.findWithLocationsAndContactsById(id).orElseThrow(() ->
                new ResourceNotFoundException("Customs office with id {0} not found", id.toString()));
        return office.getContacts();
    }

    @Transactional
    public void delete(Long id){
        CustomsOffice customsOffice = getOrThrowException(id);
        customsOffice.setDeleted(true);
        customsOffice = customsOfficeRepository.save(customsOffice);
        publisher.publishEvent(EventMessageWrapper.with(customsOffice, Operation.DELETE));
    }

    public List<CustomsOffice> getBorderCustoms(){
        return customsOfficeRepository.findByBorderCustoms(true);
    }
    
    public void validateNameExisting(CustomsOffice customsOffice) {
    	Iterable<CustomsOffice> customsOffices = customsOfficeRepository.findAll(customsOffice.toNameSpacifition());
    	if(0 < StreamSupport.stream(customsOffices.spliterator(), false).filter(i->!Objects.equals(i.getId(), customsOffice.getId())).count()) {
    		throw new BadRequestException("Customs Office names should be unique");
    	}
    }

	public Page<CustomsOffice> searchCustomsOffice(SearchCustomsOfficeJson terms) {
		return customsOfficeRepository.findAll(terms.toSpecification(), terms.paging());
	}
	
	public CustomsOfficeLocation getCustomsOfficeLocation(Long id) {
		return Optional.ofNullable(customsOfficeLocationRepository.findOne(id)).orElseThrow(ResourceNotFoundException::new);
	}
}
