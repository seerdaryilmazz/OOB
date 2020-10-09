package ekol.kartoteks.service;

import ekol.exceptions.BadRequestException;
import ekol.kartoteks.domain.Company;
import ekol.kartoteks.domain.CompanyLocation;
import ekol.kartoteks.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by kilimci on 19/06/2017.
 */
@Service
public class CompanyLocationMoveService {

    @Autowired
    private CompanyLocationRepository companyLocationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyContactRepository companyContactRepository;

    @Autowired
    private CompanyLocationIdMappingRepository companyLocationIdMappingRepository;

    @Autowired
    private CompanyIdMappingRepository companyIdMappingRepository;

   @Transactional
    public void moveTo(CompanyLocation sourceLocation, Company toCompany){
       if(sourceLocation.isDefault()){
           throw new BadRequestException("Can not move default location of a company");
       }
       Company sourceCompany = companyRepository.findOne(sourceLocation.getCompany().getId());
       if(sourceCompany.getCompanyLocations().size() == 1){
           throw new BadRequestException("Can not move the one and only location of a company");
       }

       sourceLocation.getMappedIds().forEach(locMappedId ->
                   sourceCompany.getMappedIds().stream().filter(compMappedId ->
                           compMappedId.getApplication().equals(locMappedId.getApplication()) &&
                                   compMappedId.getApplicationCompanyId().equals(locMappedId.getApplicationLocationId())
                   ).forEach(compMappedId -> {
                       compMappedId.setDeleted(true);
                       companyIdMappingRepository.save(compMappedId);
                   })

       );

       sourceCompany.getCompanyContacts().stream()
               .filter(contact  -> contact.getCompanyLocation() != null && contact.getCompanyLocation().equals(sourceLocation))
               .forEach(contact -> {
                   contact.setCompany(toCompany);
                   companyContactRepository.save(contact);
               });

       sourceLocation.setCompany(toCompany);
       companyLocationRepository.save(sourceLocation);
    }
}
