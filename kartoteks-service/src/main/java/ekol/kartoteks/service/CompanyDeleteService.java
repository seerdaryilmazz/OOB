package ekol.kartoteks.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import ekol.exceptions.BadRequestException;
import ekol.kartoteks.domain.Company;
import ekol.kartoteks.domain.CompanyLocation;
import ekol.kartoteks.repository.CompanyCustomRepository;
import ekol.kartoteks.repository.CompanyIdMappingRepository;

/**
 * Created by kilimci on 08/06/16.
 */
@Service
public class CompanyDeleteService {

    @Autowired
    private CompanyIndexingService companyIndexingService;

    @Autowired
    private CompanyIdMappingRepository idMappingRepository;

    @Autowired
    private CompanyCustomRepository companyCustomRepository;
    
    @Autowired
    private CrmAccountService crmAccountService;

    @Autowired
    private LocationService locationService;

    @Transactional
    public void delete(Company company){
        ensureCompanyCanBeDeleted(company, true);
        companyCustomRepository.deleteCompany(company);
        companyIndexingService.removeCompany(company);
    }

    @Transactional
    public void forceDelete(Company company){
        companyCustomRepository.deleteCompany(company);
        companyIndexingService.removeCompany(company);
    }

    public void ensureCompanyCanBeDeleted(Company company, boolean checkCompanyIdMapping) {
        if (checkCompanyIdMapping && !idMappingRepository.findByCompany(company).isEmpty()) {
            throw new BadRequestException("Can not delete company with reference to external systems");
        }
        if (crmAccountService.doesCompanyHaveAnAccount(company.getId())) {
            throw new BadRequestException("Since this company is related with an account in CRM, it cannot be deleted/merged!");
        }
        ensureLocationsCanBeDeleted(company.getCompanyLocations());
    }

    public void ensureLocationsCanBeDeleted(Iterable<CompanyLocation> locations) {
        List<Long> locationIds = StreamSupport.stream(locations.spliterator(), false).map(CompanyLocation::getId).filter(Objects::nonNull).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(locationIds)) {
        	return;
        }
        if (locationService.warehouseExistsByLocationIds(locationIds) || locationService.customerWarehouseExistsByLocationIds(locationIds)) {
            throw new BadRequestException("One or more locations of the company defined as Cross-Dock or General Location. They cannot be deleted/merged.");
        }
    }

    public void ensureLocationCanBeDeleted(CompanyLocation location) {
        if (locationService.warehouseExistsByLocationId(location.getId()) || locationService.customerWarehouseExistsByLocationId(location.getId())) {
            throw new BadRequestException("Location defined as Cross-Dock or General Location. It cannot be deleted/merged.");
        }
    }
}
