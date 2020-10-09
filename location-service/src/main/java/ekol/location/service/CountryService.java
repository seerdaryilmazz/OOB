package ekol.location.service;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.location.domain.Country;
import ekol.location.repository.CountryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by ozer on 13/12/16.
 */
@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    public Country findByIdOrThrowResourceNotFoundException(Long id) {

        Country persistedEntity = countryRepository.findOne(id);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Country with specified id cannot be found: " + id);
        }
    }

    public Country findByIsoOrThrowResourceNotFoundException(String iso) {

        Country persistedEntity = countryRepository.findByIso(iso);

        if (persistedEntity != null) {
            return persistedEntity;
        } else {
            throw new ResourceNotFoundException("Country with specified iso cannot be found: " + iso);
        }
    }

    @Transactional
    public Country create(Country country) {

        if (country == null) {
            throw new BadRequestException("A country must be specified.");
        }

        country.setId(null);

        if (StringUtils.isBlank(country.getName())) {
            throw new BadRequestException("A name must be specified.");
        }

        country.setName(country.getName().trim().toUpperCase());

        if (StringUtils.isBlank(country.getIso())) {
            throw new BadRequestException("An iso code must be specified.");
        }

        country.setIso(country.getIso().trim().toUpperCase());

        if (country.getIso().length() != 2) {
            throw new BadRequestException("Iso code must consist of two letters.");
        }

        if (country.getPhoneCode() == null) {
            throw new BadRequestException("A phone code must be specified.");
        }

        if (country.getPhoneCode() < 1L || country.getPhoneCode() > 999L) {
            throw new BadRequestException("Phone code must be between 1-999.");
        }

        if (StringUtils.isBlank(country.getCurrency())) {
            throw new BadRequestException("A currency must be specified.");
        }

        country.setCurrency(country.getCurrency().trim().toUpperCase());

        if (country.getCurrency().length() != 3) {
            throw new BadRequestException("Currency must consist of three letters.");
        }

        // TODO: Diğer alanları da kontrol etmek lazım.

        Country entityByName = countryRepository.findByName(country.getName());

        if (entityByName != null) {
            throw new BadRequestException("Specified name is already used.");
        }

        Country entityByIso = countryRepository.findByIso(country.getIso());

        if (entityByIso != null) {
            throw new BadRequestException("Specified iso code is already used.");
        }

        return countryRepository.save(country);
    }

    @Transactional
    public Country update(Country country) {

        if (country == null) {
            throw new BadRequestException("A country must be specified.");
        }

        if (country.getId() == null) {
            throw new BadRequestException("An id must be specified.");
        }

        Country originalData = findByIdOrThrowResourceNotFoundException(country.getId());

        if (StringUtils.isBlank(country.getName())) {
            throw new BadRequestException("A name must be specified.");
        }

        country.setName(country.getName().trim().toUpperCase());

        if (StringUtils.isBlank(country.getIso())) {
            throw new BadRequestException("An iso code must be specified.");
        }

        country.setIso(country.getIso().trim().toUpperCase());

        if (country.getIso().length() != 2) {
            throw new BadRequestException("Iso code must consist of two letters.");
        }

        if (country.getPhoneCode() == null) {
            throw new BadRequestException("A phone code must be specified.");
        }

        if (country.getPhoneCode() < 1L || country.getPhoneCode() > 999L) {
            throw new BadRequestException("Phone code must be between 1-999.");
        }

        if (StringUtils.isBlank(country.getCurrency())) {
            throw new BadRequestException("A currency must be specified.");
        }

        country.setCurrency(country.getCurrency().trim().toUpperCase());

        if (country.getCurrency().length() != 3) {
            throw new BadRequestException("Currency must consist of three letters.");
        }

        Country entityByName = countryRepository.findByName(country.getName());

        if (entityByName != null && !entityByName.getId().equals(originalData.getId())) {
            throw new BadRequestException("Specified name is already used.");
        }

        Country entityByIso = countryRepository.findByIso(country.getIso());

        if (entityByIso != null && !entityByIso.getId().equals(originalData.getId())) {
            throw new BadRequestException("Specified iso code is already used.");
        }

        // TODO: Diğer alanları da kontrol etmek lazım.

        // TODO: Diğer alanların da değiştirilmesine izin vermemiz lazım.
        originalData.setName(country.getName());
        originalData.setIso(country.getIso());
        originalData.setPhoneCode(country.getPhoneCode());
        originalData.setCurrency(country.getCurrency());
        originalData.setEuMember(country.isEuMember());

        return countryRepository.save(country);
    }

    @Transactional
    public void softDelete(Long id) {
        Country persistedEntity = findByIdOrThrowResourceNotFoundException(id);
        persistedEntity.setDeleted(true);
        countryRepository.save(persistedEntity);
    }

    public Iterable<Country> findAll() {
        return countryRepository.findAllByOrderByNameAsc();
    }
}
