package ekol.kartoteks.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ekol.kartoteks.event.CompanyDataEventMessage;
import ekol.kartoteks.event.Operation;
import ekol.kartoteks.utils.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.kartoteks.domain.*;
import ekol.kartoteks.domain.dto.IdNamePair;
import ekol.kartoteks.domain.dto.UpdateShortNameRequest;
import ekol.kartoteks.domain.validator.*;
import ekol.kartoteks.event.CompanyEvent;
import ekol.kartoteks.repository.*;
import ekol.kartoteks.repository.common.CountryRepository;
import ekol.kartoteks.repository.common.TaxOfficeRepository;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Created by kilimci on 05/04/16.
 */
@Service
public class CompanySaveService {

    @Autowired
    private CompanyValidator companyValidator;
    @Autowired
    private CompanyLocationValidator companyLocationValidator;
    @Autowired
    private CompanyContactValidator companyContactValidator;
    @Autowired
    private CompanyRoleValidator companyRoleValidator;

    @Autowired
    private CompanyCustomRepository companyCustomRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyLocationRepository companyLocationRepository;

    @Autowired
    private CompanyContactRepository companyContactRepository;

    @Autowired
    private CompanySectorRepository companySectorRepository;

    @Autowired
    private CompanyRoleRepository companyRoleRepository;

    @Autowired
    private CompanyRelationRepository companyRelationRepository;

    @Autowired
    private CompanyEmployeeRelationRepository companyEmployeeRelationRepository;

    @Autowired
    private CompanyIndexingService companyIndexingService;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private TaxOfficeRepository taxOfficeRepository;

    @Autowired
    private CompanyIdMappingRepository companyIdMappingRepository;

    @Autowired
    private CompanyLocationIdMappingRepository companyLocationIdMappingRepository;

    @Autowired
    private CompanyContactIdMappingRepository companyContactIdMappingRepository;

    @Autowired
    private CompanyDataExportService companyDataExportService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CompanyDeleteService companyDeleteService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private ImageDownloadService imageDownloadService;

    @Autowired
    private CompanyNameGenerator companyNameGenerator;

    @Autowired
    private AuthorizationService authorizationService;

    private static final String COMPANY_INCREMENT_KEY = "increment_companyCode";
    private static final int COMPANY_INCREMENT_START = 10000;

    private void sanitizeCompany(Company company){
        company.toUpperCase();
        company.setCode(produceCompanyCodeFromRedis());
        company.getCompanyContacts().forEach(CompanyContact::deleteInvalidNumbers);
    }
    private void replaceRelationsWithActual(Company company){
        company.setCountry(countryRepository.findByIsoIgnoreCase(company.getCountry().getIso()));
        if(company.getTaxOffice() != null){
            company.setTaxOffice(taxOfficeRepository.findByCode(company.getTaxOffice().getCode()));
            company.setTaxOfficeCode(company.getTaxOffice().getCode());
        }
        company.getCompanyLocations().forEach(location -> {
            location.getPostaladdress().setCountry(countryRepository.findByIsoIgnoreCase(location.getPostaladdress().getCountry().getIso()));
        });
    }
    private void validateCompany(Company company){
        companyValidator.validate(company);
        
        Supplier<Stream<CompanyLocation>> companyLocationsStream = ()->company.getCompanyLocations().stream();
        if(companyLocationsStream.get().anyMatch(CompanyLocation::isUpdated)) {
        	companyLocationsStream.get().filter(CompanyLocation::isUpdated).forEach(companyLocationValidator::validate);
        } else {
        	companyLocationsStream.get()
                .filter(companyLocation -> !companyLocation.isDeleted())
                .forEach(companyLocationValidator::validate);
        }

        company.getCompanyContacts().stream()
                .filter(companyContact -> !companyContact.isDeleted())
                .forEach(companyContactValidator::validate);

        company.getRoles().stream()
                .filter(companyRole -> !companyRole.isDeleted())
                .forEach(companyRoleValidator::validate);
    }
    
    @Transactional
    public Company save(Company company){
        sanitizeCompany(company);
        validateCompany(company);
        replaceRelationsWithActual(company);

        Company savedCompany;
        boolean isNewCompany = company.getId() == null;
        if(isNewCompany){
        	savedCompany = createCompany(company);
        }else{
        	savedCompany = updateCompany(company);
        }
        companyIndexingService.indexCompany(savedCompany);
        publisher.publishEvent(CompanyEvent.with(savedCompany));
        return savedCompany;
    }

    private Company createCompany(Company company){
        company.setLogoFilePath(downloadLogoImage(company));
        Company savedCompany = companyCustomRepository.saveCompany(company);
        companyDataExportService.export(savedCompany, false);
        return savedCompany;
    }

    public void ensureUpdateIsAllowed(Company company) {
        Map<String, Boolean> editCompanyOperations = getEditCompanyOperations();
        boolean editCompanyOperation = editCompanyOperations.get("kartoteks.company.edit-company");
        boolean editTempCompanyOperation = editCompanyOperations.get("kartoteks.company.edit-temp-company");
        boolean isTempCompany = !doesAtLeastOneLocationHaveQuadroMapping(company);
        boolean allowUpdate = editCompanyOperation || (editTempCompanyOperation && isTempCompany);

        if (!allowUpdate) {
            throw new BadRequestException("Companies which were arranged by OneCard Team, can't be updated. Please contact with onecard@ekol.com!");
        }
    }

    private Company updateCompany(Company company) {
        Company existing = companyRepository.findNoDetailsById(company.getId());
        
        companyDeleteService.ensureLocationsCanBeDeleted(findLocationsThatWillBeDeleted(existing, company));

        if(!StringUtils.equals(existing.getLogoUrl(), company.getLogoUrl())){
            company.setLogoFilePath(downloadLogoImage(company));
        }

        boolean updateCompany = companyDataExportService.isCompanyUpdateNecessary(existing, company);
        List<CompanyLocation> locationsToExport = new ArrayList<>();
        if(!updateCompany){
            locationsToExport = companyDataExportService.findLocationsNeedsToBeExported(existing, company);
        }
        Company updated = companyCustomRepository.saveCompany(company);
        if(updateCompany){
            companyDataExportService.export(updated, false);
        }else{
            locationsToExport.forEach(companyLocation -> companyDataExportService.export(updated, companyLocation, false));
        }

        return updated;
    }
    
    private List<CompanyLocation> findLocationsThatWillBeDeleted(Company existingCompany, Company updatedCompany) {
        Set<Long> idsOfLocationsThatWontBeDeleted = new HashSet<>();
        
        idsOfLocationsThatWontBeDeleted.addAll(updatedCompany.getCompanyLocations().stream().map(CompanyLocation::getId).filter(Objects::nonNull).collect(Collectors.toSet()));
        return existingCompany.getCompanyLocations().stream().filter(t->!idsOfLocationsThatWontBeDeleted.contains(t.getId())).collect(Collectors.toList());
    }

    /**
     * En az 1 mapping varsa temp company olarak kabul etmiyoruz.
     */
    private boolean doesAtLeastOneLocationHaveQuadroMapping(Company company) {
    	return company.getCompanyLocations().stream().anyMatch(t->!t.isTemp());
    }

    private Map<String,Boolean> getEditCompanyOperations() {

        List<IdNamePair> operations = authorizationService.findOperationsOfCurrentUser();

        Map<String,Boolean> result = new HashMap<>();
        result.put("kartoteks.company.edit-company", operations.stream().map(IdNamePair::getName).anyMatch(t->Objects.equals(t, "kartoteks.company.edit-company")));
        result.put("kartoteks.company.edit-temp-company", operations.stream().map(IdNamePair::getName).anyMatch(t->Objects.equals(t, "kartoteks.company.edit-temp-company")));

        return result;
    }

    @Transactional
    public Company updatePartialCompany(Company existingCompany, Company companyData){
        if(companyData.getCountry() != null) {
            companyData.setCountry(countryRepository.findByIsoIgnoreCase(companyData.getCountry().getIso()));
        }
        if(companyData.getTaxOffice() != null){
            companyData.setTaxOffice(taxOfficeRepository.findByCode(companyData.getTaxOffice().getCode()));
        }

        validateNamesIfChanged(existingCompany, companyData);

        companyDataExportService.checkDataUpdateAndExport(existingCompany, companyData);

        existingCompany.copyFrom(companyData);

        if(!StringUtils.equals(existingCompany.getLogoUrl(), companyData.getLogoUrl())){
            companyData.setLogoFilePath(downloadLogoImage(companyData));
        }

        Company savedCompany = companyRepository.save(existingCompany);
        companyCustomRepository.saveIdMappings(companyData.getMappedIds(), savedCompany);
        companyIndexingService.indexCompany(savedCompany);
        publisher.publishEvent(CompanyEvent.with(savedCompany));
        return savedCompany;
    }
    private void validateNamesIfChanged(Company existingCompany, Company companyData){
        if(companyData.getName() != null &&
                (!existingCompany.getName().equals(companyData.nameToUpperCase())) &&
                companyRepository.findByName(companyData.getName()) !=  null){
            throw new BadRequestException("Company with name {0} already exists", companyData.getName());
        }
        if(companyData.getLocalName() != null &&
                (!existingCompany.getLocalName().equals(companyData.localNameToUpperCase())) &&
                companyRepository.findByLocalName(companyData.getLocalName()) !=  null){
            throw new BadRequestException("Company with local name {0} already exists", companyData.getLocalName());
        }
    }

    @Transactional
    public CompanyLocation updateLocation(Long existingLocationId, CompanyLocation companylocationData){
        CompanyLocation existingCompanyLocation = companyLocationRepository.findOne(existingLocationId);
        if(existingCompanyLocation == null) {
            throw new ResourceNotFoundException("Location with id {0} is not found", existingLocationId);
        }
        existingCompanyLocation.copyFrom(companylocationData);
        existingCompanyLocation.toUpperCase();
        companyCustomRepository.saveIdMappings(companylocationData.getMappedIds(), existingCompanyLocation);
        companyDataExportService.checkDataUpdateAndExport(existingCompanyLocation, companylocationData);
        return companyLocationRepository.save(existingCompanyLocation);
    }

    @Transactional
    public CompanyLocation saveLocation(Company company, CompanyLocation location){
        location.setCompany(company);
        location.toUpperCase();

        CompanyLocation savedLocation = companyLocationRepository.save(location);
        companyCustomRepository.saveIdMappings(location.getMappedIds(), savedLocation);
        companyDataExportService.export(company, location, false);
        return savedLocation ;
    }

    @Transactional
    public CompanyContact updateContact(Long existingContactId, CompanyContact companyContactData){
        CompanyContact existingCompanyContact = companyContactRepository.findOne(existingContactId);
        if(existingCompanyContact == null) {
            throw new ResourceNotFoundException("Contact with id {0} is not found", existingContactId);
        }
        existingCompanyContact.copyFrom(companyContactData);
        existingCompanyContact.toUpperCase();
        companyContactValidator.validate(existingCompanyContact);
        companyCustomRepository.saveIdMappings(companyContactData.getMappedIds(), existingCompanyContact);
        return companyContactRepository.save(existingCompanyContact);
    }

    @Transactional
    public CompanyContact saveContact(Company company, CompanyContact contact){
        contact.setCompany(company);
        contact.deleteInvalidNumbers();
        contact.toUpperCase();
        companyContactValidator.validate(contact);
        CompanyContact savedContact = companyContactRepository.save(contact);
        companyCustomRepository.saveIdMappings(contact.getMappedIds(), savedContact);
        return savedContact;
    }

    @Transactional
    public List<CompanySector> updateSectors(Long companyId, List<CompanySector> sectors) {
        Company company = companyRepository.findById(companyId);
        if(company == null) {
            throw new ResourceNotFoundException();
        }
        if(sectors == null) {
            throw new BadRequestException("company sectors is null");
        }
        List<CompanySector> existingSectors = new ArrayList<>(company.getSectors());
        boolean isNecessaryUpdate = hasSectorsUpdatesToExport(existingSectors, sectors);
        List<CompanySector> finalSectors = sectors.stream().map(
                sector -> {
                    if(sector.getId() == null){
                        return saveSector(company, sector);
                    }else{
                        return updateSector(sector.getId(), sector);
                    }

                })
                .collect(Collectors.toList());
        // TODO: Şuan için sektör bilgilerinin değişikliği export işleminin oluşmasını etkilemiyor. Eğer etkileyecekse Company.hasUpdatesToExport metodu da gözden geçirilmeli ve aşağıda export işlemi yapılmalı.
        // TODO: Şuan için Elasticsearch indeksinde sektör bilgileri tutulmadığından bilinçli olarak CompanyIndexingService.indexCompany metodu çağırılmadı. Bu durum değişirse aşağıda index işlemi yapılmalı.
        if (isNecessaryUpdate) {
            publisher.publishEvent(CompanyDataEventMessage.createWith(company, Operation.AFTER_SECTORS_CHANGED));
        }
        publisher.publishEvent(CompanyEvent.with(company));
        return finalSectors;
    }

    @TransactionalEventListener(fallbackExecution = true, condition = "#message.operation == T(ekol.kartoteks.event.Operation).AFTER_SECTORS_CHANGED")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCompanyAfterSectorsChanged(CompanyDataEventMessage message){
        Company updatedCompany = companyRepository.findById(message.getId());
        companyDataExportService.export(updatedCompany, false);
    }

    private boolean hasSectorsUpdatesToExport(List<CompanySector> existingSectors, List<CompanySector> updatedSectors) {
        return ObjectUtils.areDifferent(existingSectors,updatedSectors,ObjectUtils.COMPANY_SECTOR_COMPARATOR);
    }

    @Transactional
    public CompanySector updateSector(Long existingSectorId, CompanySector companySectorData){
        CompanySector existingCompanySector = companySectorRepository.findOne(existingSectorId);
        if(existingCompanySector == null) {
            throw new ResourceNotFoundException("Sector with id {0} is not found", existingSectorId);
        }
        existingCompanySector.setDefault(companySectorData.isDefault());
        existingCompanySector.setSector(companySectorData.getSector());
        return companySectorRepository.save(existingCompanySector);
    }

    @Transactional
    public CompanySector saveSector(Company company, CompanySector sector){
        sector.setCompany(company);
        return companySectorRepository.save(sector);
    }

    @Transactional
    public CompanyRelation updateRelation(Long existingRelationId, CompanyRelation companyRelationData){
        CompanyRelation existingCompanyRelation = companyRelationRepository.findOne(existingRelationId);
        if(existingCompanyRelation == null) {
            throw new ResourceNotFoundException("Relation with id {0} is not found", existingRelationId);
        }
        existingCompanyRelation.setActiveCompany(companyRelationData.getActiveCompany());
        existingCompanyRelation.setPassiveCompany(companyRelationData.getPassiveCompany());
        existingCompanyRelation.setRelationType(companyRelationData.getRelationType());
        return companyRelationRepository.save(existingCompanyRelation);
    }

    @Transactional
    public CompanyRelation saveRelation(Company company, CompanyRelation companyRelation){
        companyRelation.setActiveCompany(company);
        return companyRelationRepository.save(companyRelation);
    }

    @Transactional
    public CompanyRole updateRole(Long existingRoleId, CompanyRole companyRoleData){
        CompanyRole existingCompanyRole = companyRoleRepository.findOne(existingRoleId);
        if(existingCompanyRole == null) {
            throw new ResourceNotFoundException("Role with id {0} is not found", existingRoleId);
        }
        existingCompanyRole.setDeleted(companyRoleData.isDeleted());
        existingCompanyRole.setRoleType(companyRoleData.getRoleType());
        existingCompanyRole.setSegmentType(companyRoleData.getSegmentType());
        existingCompanyRole.setDateRange(companyRoleData.getDateRange());
        companyRoleData.getEmployeeRelations().forEach(companyEmployeeRelation -> {
            if(companyEmployeeRelation.getId() == null) {
                saveEmployeeRelation(existingCompanyRole, companyEmployeeRelation);
            }else{
                updateEmployeeRelation(companyEmployeeRelation.getId(), companyEmployeeRelation);
            }
        });

        return companyRoleRepository.save(existingCompanyRole);
    }

    @Transactional
    public CompanyRole saveRole(Company company, CompanyRole role){
        role.setCompany(company);
        CompanyRole saved = companyRoleRepository.save(role);
        role.getEmployeeRelations().forEach(companyEmployeeRelation -> {
            companyEmployeeRelation.setCompanyRole(saved);
            companyEmployeeRelationRepository.save(companyEmployeeRelation);
        });
        return saved;
    }

    @Transactional
    public CompanyEmployeeRelation updateEmployeeRelation(Long existingRelationId, CompanyEmployeeRelation employeeRelationData){
        CompanyEmployeeRelation existingRelation = companyEmployeeRelationRepository.findOne(existingRelationId);
        if(existingRelation == null) {
            throw new ResourceNotFoundException("Relation with id {0} is not found", existingRelationId);
        }
        existingRelation.setDeleted(employeeRelationData.isDeleted());
        existingRelation.setEmployeeAccount(employeeRelationData.getEmployeeAccount());
        existingRelation.setRelation(employeeRelationData.getRelation());

        return companyEmployeeRelationRepository.save(existingRelation);
    }

    @Transactional
    public CompanyEmployeeRelation saveEmployeeRelation(CompanyRole role, CompanyEmployeeRelation companyEmployeeRelation){
        companyEmployeeRelation.setCompanyRole(role);
        return companyEmployeeRelationRepository.save(companyEmployeeRelation);
    }

    private Long produceCompanyCodeFromRedis(){
        redisTemplate.opsForValue().setIfAbsent(COMPANY_INCREMENT_KEY, String.valueOf(COMPANY_INCREMENT_START));
        redisTemplate.opsForValue().increment(COMPANY_INCREMENT_KEY,1);
        return Long.valueOf(redisTemplate.opsForValue().get(COMPANY_INCREMENT_KEY));
    }

    @Transactional
    public void mergeCompanies(Company company, Company otherCompany) {
        companyDeleteService.ensureCompanyCanBeDeleted(otherCompany, false);
        companyDeleteService.forceDelete(otherCompany);
        save(company);
    }

    public Company downloadLogoImage(Long companyId) {
        Company company = companyRepository.findOne(companyId);
        if(company == null){
            return null;
        }
        if(StringUtils.isNotBlank(company.getLogoUrl())){
            String filePath = downloadLogoImage(company);
            company.setLogoFilePath(filePath);
            companyRepository.save(company);
        }
        return company;
    }
    private String downloadLogoImage(Company company) {
        if(StringUtils.isNotBlank(company.getLogoUrl())){
            return imageDownloadService.downloadAndSave(company.getLogoUrl());
        }
        return null;
    }

    public void downloadAllImages() {
        Logger logger = LoggerFactory.getLogger(CompanySaveService.class);
        List<Company> companies = companyRepository.findByLogoUrlNotNull();
        companies.forEach(company -> {
            String filePath = null;
            try {
                filePath = downloadLogoImage(company);
            }catch(Exception e){
                logger.error("Error downloading image", e);
                //ignore errors when batch processing
            }
            if(filePath != null){
                company.setLogoFilePath(filePath);
                companyRepository.save(company);
            }
        });
    }

    public void assignShortNamesFoAll() {
        List<Company> companies = companyRepository.findByShortNameIsNull();
        companies.forEach(company -> {
            company.setShortName(companyNameGenerator.generateShortName(company.getId(), company.getName()));
            companyRepository.save(company);
        });
        List<CompanyLocation> locations = companyLocationRepository.findByShortNameIsNull();
        locations.forEach(location -> {
            location.setShortName(companyNameGenerator.generateLocationShortName(
                    location.getId(),
                    location.getCompany().getShortName(),
                    location.getPostaladdress().getCity(),
                    location.getPostaladdress().getDistrict(),
                    null));

            companyLocationRepository.save(location);
        });

    }

    @Transactional
    public void updateShortNames(UpdateShortNameRequest request) {
        Company company = companyRepository.findById(request.getCompanyId());
        if(company == null){
            throw new ResourceNotFoundException("Company with id {0} not found", request.getCompanyId());
        }
        company.setShortName(request.getShortName());
        company.setShortNameChecked(true);

        request.getLocationShortNames().forEach(this::updateLocationShortName);

        companyValidator.validate(company);
        Company savedCompany = companyRepository.save(company);
        companyIndexingService.indexCompany(savedCompany);
        companyDataExportService.export(savedCompany, false);
    }

    public void updateLocationShortName(Long locationId, String shortName) {
        CompanyLocation companyLocation = companyLocationRepository.findOne(locationId);
        if(companyLocation == null){
            throw new ResourceNotFoundException("Company location with id {0} not found", locationId);
        }
        companyLocation.setName(shortName);
        companyLocation.setShortName(shortName);
        companyLocation.setShortNameChecked(true);
        companyLocationRepository.save(companyLocation);
    }

    public void confirmPointOnMap(Long locationId, BigDecimal lat, BigDecimal lng){
        CompanyLocation location = companyLocationRepository.findOne(locationId);
        if(location == null){
            throw new ResourceNotFoundException("Locatipn with id {0} not found", locationId);
        }
        location.getPostaladdress().getPointOnMap().setLat(lat);
        location.getPostaladdress().getPointOnMap().setLng(lng);
        location.setPointOnMapConfirmed(true);
        companyLocationRepository.save(location);
    }
}
