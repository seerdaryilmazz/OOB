package ekol.kartoteks.repository;

import ekol.kartoteks.domain.*;
import ekol.kartoteks.repository.common.CountryRepository;
import ekol.kartoteks.repository.common.TaxOfficeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by fatmaozyildirim on 4/5/16.
 */
@Repository
public class CompanyCustomRepository {


    @Autowired
    private CompanyContactRepository companyContactRepository;
    @Autowired
    private CompanyLocationRepository companyLocationRepository;
    @Autowired
    private CompanyRoleRepository companyRoleRepository;
    @Autowired
    private CompanyEmployeeRelationRepository companyEmployeeRelationRepository;
    @Autowired
    private CompanySectorRepository companySectorRepository;
    @Autowired
    private CompanyRelationRepository companyRelationRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyIdMappingRepository companyIdMappingRepository;
    @Autowired
    private CompanyLocationIdMappingRepository companyLocationIdMappingRepository;
    @Autowired
    private CompanyContactIdMappingRepository companyContactIdMappingRepository;
    @Autowired
    private TaxOfficeRepository taxOfficeRepository;
    @Autowired
    private CountryRepository countryRepository;

    public CompanyCustomRepository() {
        //Default Constructor
    }

    @Transactional
    public Company saveCompany(Company input){
        boolean isNewCompany = input.getId() == null;
        return isNewCompany ? createCompany(input) : updateCompany(input);
    }

    private Company createCompany(Company input){
        Company company = companyRepository.save(input);
        saveRelationsForCompany(input, company);
        return company;
    }

    private Company updateCompany(Company input){
        Company company = companyRepository.findNoDetailsById(input.getId());
        saveRelationsForCompany(input, company);
        return companyRepository.save(input);
    }

    private void saveRelationsForCompany(Company input, Company savedCompany){
        saveIdMappings(input.getMappedIds(), savedCompany);
        saveLocationsForCompany(input.getCompanyLocations(), input.getCompanyContacts(), savedCompany);
        saveContactsForCompany(input.getCompanyContacts(), savedCompany);
        saveRolesForCompany(input.getRoles(), savedCompany);
        saveSectorsForCompany(input.getSectors(), savedCompany);
        saveRelationsForCompany(input.getActiveRelations(), input.getPassiveRelations(), savedCompany);
    }

    private void saveLocationsForCompany(Set<CompanyLocation> locations, Set<CompanyContact> contacts, Company company){
        deletedRemovedLocations(locations, contacts, company);
        locations.forEach(companyLocation -> {
            companyLocation.setCompany(company);
            companyLocation.setDeleted(false);
            CompanyLocation saved = companyLocationRepository.save(companyLocation);
            saveIdMappings(companyLocation.getMappedIds(), saved);
            setSavedLocationToContacts(company.getCompanyContacts(), saved);
            setSavedLocationToContacts(contacts, saved);
            // mappedIds listesini bilinçli olarak doldurduk, çünkü ileriki adımlarda bu alanlar return edilen company nesnesi üzerinden okunacak.
            saved.setMappedIds(new HashSet<>());
            for (CompanyLocationIdMapping idMapping : companyLocationIdMappingRepository.findByCompanyLocation(saved)) {
                saved.getMappedIds().add(idMapping);
            }
        });
    }
    private void deletedRemovedLocations(Set<CompanyLocation> locations, Set<CompanyContact> contacts, Company company){
        company.getCompanyLocations().forEach(companyLocation -> {
            Long count = locations.stream().filter(companyLocation::equals).count();
            if(count == 0){
                companyLocation.setDeleted(true);
                contacts.forEach(contact -> {
                    if(contact.getCompanyLocation().equals(companyLocation)){
                        contact.setCompanyLocation(null);
                        companyContactRepository.save(contact);
                    }
                });
                companyLocationRepository.save(companyLocation);
            }
        });
    }
    private void saveContactsForCompany(Set<CompanyContact> contacts, Company company){
        deletedRemovedContacts(contacts, company);
        contacts.forEach(companyContact -> {
            companyContact.setCompany(company);
            CompanyContact saved = companyContactRepository.save(companyContact);
            saveIdMappings(companyContact.getMappedIds(), saved);
            // mappedIds listesini bilinçli olarak doldurduk, çünkü ileriki adımlarda bu alanlar return edilen company nesnesi üzerinden okunacak.
            saved.setMappedIds(new HashSet<>());
            for (CompanyContactIdMapping idMapping : companyContactIdMappingRepository.findByCompanyContact(saved)) {
                saved.getMappedIds().add(idMapping);
            }
        });
    }
    private void deletedRemovedContacts(Set<CompanyContact> contacts, Company company){
        company.getCompanyContacts().forEach(companyContact -> {
            Long count = contacts.stream().filter(companyContact::equals).count();
            if(count == 0){
                companyContact.setDeleted(true);
                companyContactRepository.save(companyContact);
            }
        });
    }
    private void saveRolesForCompany(Set<CompanyRole> roles, Company company){
        deletedRemovedRoles(roles, company);
        roles.forEach(companyRole -> {
            companyRole.setCompany(company);
            CompanyRole savedRole = companyRoleRepository.save(companyRole);
            companyRole.getEmployeeRelations().forEach(companyEmployeeRelation -> {
                companyEmployeeRelation.setCompanyRole(savedRole);
                companyEmployeeRelationRepository.save(companyEmployeeRelation);
            });
        });
    }
    private void deletedRemovedRoles(Set<CompanyRole> roles, Company company){
        company.getRoles().forEach(companyRole -> {
            Long count = roles.stream().filter(companyRole::equals).count();
            if(count == 0){
                companyRole.setDeleted(true);
                companyRoleRepository.save(companyRole);
                companyRole.getEmployeeRelations().forEach(companyEmployeeRelation -> {
                    companyEmployeeRelation.setDeleted(true);
                    companyEmployeeRelationRepository.save(companyEmployeeRelation);
                });
            }
        });
    }
    private void saveSectorsForCompany(Set<CompanySector> sectors, Company company){
        deletedRemovedSectors(sectors, company);
        sectors.forEach(companySector -> {
            companySector.setCompany(company);
            companySectorRepository.save(companySector);
        });
    }
    private void deletedRemovedSectors(Set<CompanySector> sectors, Company company){
        company.getSectors().forEach(companySector -> {
            Long count = sectors.stream().filter(companySector::equals).count();
            if(count == 0){
                companySector.setDeleted(true);
                companySectorRepository.save(companySector);
            }
        });
    }
    private void saveRelationsForCompany(Set<CompanyRelation> activeRelations, Set<CompanyRelation> passiveRelations, Company company){
        deletedRemovedActiveRelations(activeRelations, company);
        activeRelations.forEach(companyRelation -> {
            companyRelation.setActiveCompany(company);
            companyRelationRepository.save(companyRelation);
        });


        deletedRemovedPassiveRelations(passiveRelations, company);
        passiveRelations.forEach(companyRelation -> {
            companyRelation.setPassiveCompany(company);
            companyRelationRepository.save(companyRelation);
        });
    }

    private void deletedRemovedActiveRelations(Set<CompanyRelation> activeRelations, Company company){
        company.getActiveRelations().forEach(companyRelation -> {
            Long count = activeRelations.stream().filter(companyRelation::equals).count();
            if(count == 0){
                companyRelation.setDeleted(true);
                companyRelationRepository.save(companyRelation);
            }
        });
    }
    private void deletedRemovedPassiveRelations(Set<CompanyRelation> passiveRelations, Company company){
        company.getPassiveRelations().forEach(companyRelation -> {
            Long count = passiveRelations.stream().filter(companyRelation::equals).count();
            if(count == 0){
                companyRelation.setDeleted(true);
                companyRelationRepository.save(companyRelation);
            }
        });
    }

    private void setSavedLocationToContacts(Set<CompanyContact> contacts, CompanyLocation location){
        if(contacts != null){
            contacts.forEach(item -> {
                String locationName = item.getCompanyLocation() != null ? item.getCompanyLocation().getName() : null;
                if(location.getName().equals(locationName)){
                    item.setCompanyLocation(location);
                }
            });
        }
    }

    public void saveIdMappings(Set<CompanyIdMapping> idMappings, Company existingCompany){
        if(idMappings != null && !idMappings.isEmpty()){
            idMappings.stream()
                    .filter(companyIdMapping -> companyIdMappingRepository.findByCompanyAndApplicationAndApplicationCompanyId(
                            existingCompany, companyIdMapping.getApplication(), companyIdMapping.getApplicationCompanyId()) == null)
                    .forEach(companyIdMapping -> {
                        companyIdMapping.setCompany(existingCompany);
                        companyIdMappingRepository.save(companyIdMapping);
                    });
            idMappings.removeIf(companyIdMapping -> companyIdMapping.getId() == null);
        }
    }
    public void saveIdMappings(Set<CompanyLocationIdMapping> idMappings, CompanyLocation existingLocation){
        if(idMappings != null && !idMappings.isEmpty()){
            idMappings.stream()
                    .filter(locationIdMapping ->
                            companyLocationIdMappingRepository.findByCompanyLocationAndApplicationAndApplicationLocationId(
                                    existingLocation, locationIdMapping.getApplication(), locationIdMapping.getApplicationLocationId()) == null)
                    .forEach(locationIdMapping -> {
                        locationIdMapping.setCompanyLocation(existingLocation);
                        companyLocationIdMappingRepository.save(locationIdMapping);
                    });
            idMappings.removeIf(locationIdMapping -> locationIdMapping.getId() == null);
        }
    }
    public void saveIdMappings(Set<CompanyContactIdMapping> idMappings, CompanyContact existingContact){
        if(idMappings != null && !idMappings.isEmpty()){
            idMappings.forEach(idMapping -> {
                if(StringUtils.isBlank(idMapping.getApplicationContactId())){
                    return;
                }
                boolean mappingExists =
                        companyContactIdMappingRepository.findByCompanyContactAndApplicationAndApplicationContactId(
                                existingContact, idMapping.getApplication(), idMapping.getApplicationContactId()) != null;
                if(!mappingExists){
                    idMapping.setCompanyContact(existingContact);
                    companyContactIdMappingRepository.save(idMapping);
                }
            });
        }
    }

    @Transactional
    public Company deleteCompany(Company company){

        company.setDeleted(true);
        company.getCompanyLocations().forEach(companyLocation -> companyLocation.setDeleted(true));
        company.getCompanyContacts().forEach(companyContact -> companyContact.setDeleted(true));
        company.getSectors().forEach(companySector -> companySector.setDeleted(true));
        company.getRoles().forEach(role -> role.setDeleted(true));
        company.getMappedIds().forEach(companyIdMapping -> companyIdMapping.setDeleted(true));
        company.getActiveRelations().forEach(companyRelation -> companyRelation.setDeleted(true));
        company.getPassiveRelations().forEach(companyRelation -> companyRelation.setDeleted(true));

        Company savedCompany = companyRepository.save(company);
        company.getCompanyLocations().forEach(item -> {
            CompanyLocation saved = companyLocationRepository.save(item);
            setSavedLocationToContacts(company.getCompanyContacts(), saved);
            saved.getMappedIds().forEach(companyLocationIdMapping -> {
                companyLocationIdMapping.setDeleted(true);
                companyLocationIdMappingRepository.save(companyLocationIdMapping);
            });

        });
        company.getCompanyContacts().forEach(item -> {
            CompanyContact saved = companyContactRepository.save(item);
            saved.getMappedIds().forEach(companyContactIdMapping -> {
                companyContactIdMapping.setDeleted(true);
                companyContactIdMappingRepository.save(companyContactIdMapping);
            });
        });
        
        company.getRoles().forEach(companyRoleRepository::save);
        company.getSectors().forEach(companySectorRepository::save);
        company.getActiveRelations().forEach(companyRelationRepository::save);
        company.getPassiveRelations().forEach(companyRelationRepository::save);
        company.getMappedIds().forEach(companyIdMappingRepository::save);

        return savedCompany;
    }

    public Company findById(Long companyId) {
        Company company = companyRepository.findNoDetailsById(companyId);
        if(company == null){
            return null;
        }
        Set<CompanyLocation> locations = companyLocationRepository.findByCompanyId(companyId);
        Set<CompanyContact> contacts = companyContactRepository.findByCompanyId(companyId);
        List<CompanyRole> roles = companyRoleRepository.findByCompanyId(companyId);
        List<CompanySector> sectors = companySectorRepository.findByCompanyId(companyId);
        List<CompanyRelation> passiveRelations = companyRelationRepository.findByPassiveCompany(company);
        List<CompanyRelation> activeRelations = companyRelationRepository.findByActiveCompany(company);
        List<CompanyIdMapping> idMappings = companyIdMappingRepository.findByCompany(company);
        if(company.getTaxOffice() != null){
            company.setTaxOffice(taxOfficeRepository.findOne(company.getTaxOffice().getId()));
        }
        if(company.getCountry() != null){
            company.setCountry(countryRepository.findOne(company.getCountry().getId()));
        }
        contacts.forEach(CompanyContact::getCompanyLocation);
        company.setCompanyLocations(locations);
        company.setCompanyContacts(contacts);
        company.setRoles(new HashSet<>(roles));
        company.setSectors(new HashSet<>(sectors));
        company.setPassiveRelations(new HashSet<>(passiveRelations));
        company.setActiveRelations(new HashSet<>(activeRelations));
        company.setMappedIds(new HashSet<>(idMappings));
        return company;
    }


}
