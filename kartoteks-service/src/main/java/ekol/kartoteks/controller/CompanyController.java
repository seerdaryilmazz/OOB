package ekol.kartoteks.controller;

import java.util.*;
import java.util.stream.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import ekol.event.auth.Authorize;
import ekol.exceptions.*;
import ekol.kartoteks.domain.*;
import ekol.kartoteks.domain.dto.*;
import ekol.kartoteks.domain.validator.*;
import ekol.kartoteks.repository.*;
import ekol.kartoteks.service.*;
import ekol.kartoteks.utils.ServiceUtils;
import io.micrometer.core.annotation.Timed;

/**
 * Created by kilimci on 14/03/16.
 */

@RestController
@RequestMapping("/company")
public class CompanyController {

	private static final String COMPANY_ROLETYPE_PARTNER_CODE = "PARTNER";

    @Autowired
    private CompanySaveService companySaveService;
    @Autowired
    private CompanyValidator companyValidator;
    @Autowired
    private CompanyLocationValidator companyLocationValidator;
    @Autowired
    private CompanyContactValidator companyContactValidator;
    @Autowired
    private CompanyDeleteService companyDeleteService;
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
    private CompanyRoleTypeRepository companyRoleTypeRepository;
    @Autowired
    private LocationTypeRepository locationTypeRepository;
    @Autowired
    private CompanyDataExportService dataExportService;
    @Autowired
    private CompanyCustomRepository companyCustomRepository;
    @Autowired
    private CompanyNameGenerator companyNameGenerator;
    @Autowired
    private VerifyService verifyService;

    private void checkCompanyIsNull(Company company){
        if(company == null) {
            throw new BadRequestException("company is null");
        }
    }
    
    @Timed(value = "kartoteks.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @Authorize(operations = "kartoteks.company.create-company")
    @PostMapping
    public Company create(@RequestBody Company company) {
        checkCompanyIsNull(company);
        return companySaveService.save(company);
    }

    @Timed(value = "kartoteks.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @GetMapping("/{companyId}")
    public Company get(@PathVariable Long companyId) {
    	return Optional.ofNullable(companyCustomRepository.findById(companyId)).orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping("/by-location")
    public Company getByLocation(@RequestParam(value = "locationId") Long locationId) {
        CompanyLocation location = Optional.ofNullable(companyLocationRepository.findOne(locationId))
        		.orElseThrow(()->new ResourceNotFoundException("Company location with id {0} cannot be found", locationId));
        return companyRepository.findById(location.getCompany().getId());
    }

    @GetMapping("/{commaSeparatedIds}/id-and-name")
    public List<IdNamePair> getIdNamePairs(@PathVariable String commaSeparatedIds) {

        List<IdNamePair> result = new ArrayList<>();
        List<Long> ids = ServiceUtils.commaSeparatedNumbersToLongList(commaSeparatedIds);
        Map<Long, IdNamePair> idNamePairMap = companyRepository.findByIdIn(ids).stream().collect(Collectors.toMap(Company::getId, c->new IdNamePair(c.getId(), c.getName()), (x,y)->x));

        for (Long id : ids) {
            IdNamePair pair = Optional.ofNullable(idNamePairMap.get(id)).orElseThrow(()->new ResourceNotFoundException("Company with id {0} is not found", id));
            result.add(pair);
        }

        return result;
    }

    @Timed(value = "kartoteks.rest", percentiles = {0.1, 0.5, 0.9}, histogram = true)
    @Authorize(operations = {"kartoteks.company.edit-company", "kartoteks.company.edit-temp-company"})
    @PutMapping("/{companyId}")
    public Company update(@PathVariable Long companyId, @RequestBody Company company){
        checkCompanyIsNull(company);
        if (!companyRepository.exists(companyId)) {
            throw new ResourceNotFoundException("Company with id {0} is not found", companyId);
        }
        return companySaveService.save(company);
    }

    @Authorize(operations = {"kartoteks.company.edit-company", "kartoteks.company.edit-temp-company"})
    @GetMapping("/{companyId}/ensure-update-is-allowed")
    public void ensureUpdateIsAllowed(@PathVariable Long companyId) {
    	Company company = Optional.ofNullable(companyRepository.findById(companyId)).orElseThrow(()->new ResourceNotFoundException("Company with id {0} is not found", companyId));
        companySaveService.ensureUpdateIsAllowed(company);
    }

    /**
     * Bu metodun kullanıldığı bir yer bulamadık, bu nedenle şimdilik içini kapattık.
     */
    @Authorize(operations = "kartoteks.company.edit-company")
    @PutMapping("/{companyId}/company")
    public Company updateCompany(@PathVariable Long companyId, @RequestBody Company company){
        throw new BadRequestException("This operation is suspended, if you think it is necessary contact with the development team.");
//        checkCompanyIsNull(company);
//        Company existingCompany = companyRepository.findOne(companyId);
//        if (existingCompany == null) {
//            throw new ResourceNotFoundException("Company with id {0} is not found", companyId);
//        }
//        return companySaveService.updatePartialCompany(existingCompany, company);
    }

    @Authorize(operations = {"kartoteks.company.edit-company", "kartoteks.company.edit-temp-company", "kartoteks.company.create-company"})
    @PutMapping("/validate-company")
    public void validateCompany(@RequestBody Company company){
        checkCompanyIsNull(company);
        companyValidator.validateCompany(company);
    }

    @Authorize(operations = "kartoteks.company.delete-company")
    @DeleteMapping("/{companyId}")
    public void delete(@PathVariable Long companyId){
        Company company = Optional.ofNullable(companyRepository.findById(companyId)).orElseThrow(ResourceNotFoundException::new);
        companyDeleteService.delete(company);
    }

    @GetMapping("/{companyId}/ensure-can-be-deleted")
    public void ensureCompanyCanBeDeleted(@PathVariable Long companyId, @RequestParam boolean checkCompanyIdMapping) {
    	Company company = Optional.ofNullable(companyRepository.findById(companyId)).orElseThrow(ResourceNotFoundException::new);
        companyDeleteService.ensureCompanyCanBeDeleted(company, checkCompanyIdMapping);
    }

    @Authorize(operations = "kartoteks.company.force-delete-company")
    @DeleteMapping("/{companyId}/force-delete")
    public void forceDelete(@PathVariable Long companyId){
    	Company company = Optional.ofNullable(companyRepository.findById(companyId)).orElseThrow(ResourceNotFoundException::new);
        companyDeleteService.forceDelete(company);
    }

    @GetMapping("/{companyId}/locations")
    public Set<CompanyLocation> getLocations(@PathVariable Long companyId){
        return companyLocationRepository.findByCompanyId(companyId);
    }
    
    @GetMapping("/{companyId}/active-locations")
    public Set<CompanyLocation> getActiveLocations(@PathVariable Long companyId){
    	return companyLocationRepository.findByCompanyIdAndActiveTrue(companyId);
    }

    @GetMapping("/{companyId}/default-location")
    public CompanyLocation getDefaultLocation(@PathVariable Long companyId){
    	return Optional.ofNullable(companyLocationRepository.findByCompanyIdAndIsDefault(companyId, true))
    			.orElseThrow(() -> new ResourceNotFoundException("No default location of company with company id: {0}", companyId ));
    }

    @GetMapping("/default-locations")
    public List<CompanyLocation> getDefaultLocations(@RequestParam String commaSeparatedCompanyIds) {
        List<Long> companyIds = ServiceUtils.commaSeparatedNumbersToLongList(commaSeparatedCompanyIds);
        if (CollectionUtils.isEmpty(companyIds)) {
            throw new BadRequestException("At least one company id is required.");
        }
        return companyLocationRepository.findByCompanyIdInAndIsDefault(companyIds, true);
    }

    @Authorize(operations = {"kartoteks.company.edit-company"})
    @PutMapping("/{companyId}/locations")
    public List<CompanyLocation> updateLocations(@PathVariable Long companyId, @RequestBody List<CompanyLocation> companyLocations){
    	Company company = Optional.ofNullable(companyRepository.findById(companyId)).orElseThrow(ResourceNotFoundException::new);
        if(companyLocations == null) {
            throw new BadRequestException("company location is null");
        }
        return companyLocations.stream().map(
                location -> {
                    if(location.getId() == null){
                        return companySaveService.saveLocation(company, location);
                    }else{
                        return companySaveService.updateLocation(location.getId(), location);
                    }

                })
                .collect(Collectors.toList());
    }
    @Authorize(operations = {"kartoteks.company.edit-company", "kartoteks.company.edit-temp-company", "kartoteks.company.create-company"})
    @PutMapping("/validate-locations")
    public void validateCompanyLocations(@RequestBody List<CompanyLocation> locations){
        if(locations == null) {
            throw new BadRequestException("CompanyLocation is null");
        }
        locations.forEach(companyLocation -> companyLocationValidator.validate(companyLocation));
        companyValidator.validateLocations(locations);
    }

    @Authorize(operations = {"kartoteks.company.edit-company", "kartoteks.company.edit-temp-company", "kartoteks.company.create-company"})
    @PutMapping("/validate-sectors")
    public void validateCompanySectors(@RequestBody List<CompanySector> sectors){
        companyValidator.validateSectors(sectors);
    }

    @Authorize(operations = {"kartoteks.company.edit-company", "kartoteks.company.edit-temp-company", "kartoteks.company.create-company"})
    @PutMapping("/validate-contacts")
    public void validateContacts(@RequestBody List<CompanyContact> contacts) {
        if (CollectionUtils.isEmpty(contacts)) {
            throw new BadRequestException("List is empty.");
        }
        contacts.forEach(contact -> companyContactValidator.validate(contact));
    }

    @GetMapping("/{companyId}/warehouses")
    public List<CompanyLocation> geWarehouses(@PathVariable Long companyId){
        Company company = Optional.ofNullable(companyRepository.findOne(companyId)).orElseThrow(ResourceNotFoundException::new);
        LocationType warehouse = Optional.ofNullable(locationTypeRepository.findByCode("WAREHOUSE")).orElseThrow(ResourceNotFoundException::new);
        return companyLocationRepository.findByCompanyIdAndLocationTypes(company.getId(), warehouse);
    }

    @GetMapping("/{companyId}/contacts")
    public Set<CompanyContact> getContacts(@PathVariable Long companyId){
        Company company = Optional.ofNullable(companyRepository.findById(companyId)).orElseThrow(ResourceNotFoundException::new);
        return companyContactRepository.findByCompanyId(company.getId());
    }

    @Authorize(operations = "kartoteks.company.edit-company")
    @PutMapping("/{companyId}/contacts")
    public List<CompanyContact> updateContacts(@PathVariable Long companyId, @RequestBody List<CompanyContact> companyContacts){
    	Company company = Optional.ofNullable(companyRepository.findById(companyId)).orElseThrow(ResourceNotFoundException::new);
        if(companyContacts == null) {
            throw new BadRequestException("company contacts is null");
        }
        return companyContacts.stream().map(
                contact -> {
                    if(contact.getId() == null){
                        return companySaveService.saveContact(company, contact);
                    }else{
                        return companySaveService.updateContact(contact.getId(), contact);
                    }

                })
                .collect(Collectors.toList());
    }

    @Authorize(operations = "kartoteks.company.edit-company")
    @PutMapping("/{companyId}/roles")
    public List<CompanyRole> updateRoles(@PathVariable Long companyId, @RequestBody List<CompanyRole> roles){
    	Company company = Optional.ofNullable(companyRepository.findById(companyId)).orElseThrow(ResourceNotFoundException::new);
        if(roles == null) {
            throw new BadRequestException("company roles is null");
        }
        return roles.stream().map(
                role -> {
                    if(role.getId() == null){
                        return companySaveService.saveRole(company, role);
                    }else{
                        return companySaveService.updateRole(role.getId(), role);
                    }

                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{companyId}/sectors")
    public List<CompanySector> getCompanySectors(@PathVariable Long companyId){
    	Company company = Optional.ofNullable(companyRepository.findById(companyId)).orElseThrow(ResourceNotFoundException::new);
        return companySectorRepository.findByCompanyId(company.getId());
    }

    @Authorize(operations = {"kartoteks.company.edit-company", "kartoteks.company.edit-temp-company"})
    @PutMapping("/{companyId}/sectors")
    public List<CompanySector> updateSectors(@PathVariable Long companyId, @RequestBody List<CompanySector> sectors){
        return companySaveService.updateSectors(companyId, sectors);
    }

    @GetMapping("/{companyId}/roles")
    public List<CompanyRole> getRoles(@PathVariable Long companyId){
    	Company company = Optional.ofNullable(companyRepository.findById(companyId)).orElseThrow(ResourceNotFoundException::new);
        return companyRoleRepository.findByCompanyId(company.getId());
    }

    @Authorize(operations = "kartoteks.company.export-company")
    @GetMapping("/{companyId}/export")
    public void export(@PathVariable Long companyId){
        Company company = companyRepository.findById(companyId);
        if(company == null){
            throw new ResourceNotFoundException();
        }
        dataExportService.export(company, true);
    }

    @GetMapping("/owned-by-ekol")
    public List<Company> getEkolOwnedCompanies(){
        return companyRepository.findByOwnedByEkol(true);
    }

    /**
     * Bu metodun kullanıldığı bir yer bulamadık, bu nedenle şimdilik içini kapattık.
     */
    @GetMapping("/bulkExport")
    public void export(@RequestParam List<String> ids){
        throw new BadRequestException("This operation is suspended, if you think it is necessary contact with the development team.");
//        ids.forEach(id -> dataExportService.export(id.trim()));
    }

    @GetMapping("/partners")
    public List<Company> findAgentOrLogisticPartner() {
        CompanyRoleType companyRoleType = companyRoleTypeRepository.findOneByCode(COMPANY_ROLETYPE_PARTNER_CODE);
        if(companyRoleType == null) {
            throw new ResourceNotFoundException("Role Type PARTNER Not Found.");
        }

        List<CompanyRole> roles = companyRoleRepository.findByRoleType(companyRoleType);
        Set<Long> uniqueIds = new HashSet<>();
        return roles.stream().map(CompanyRole::getCompany).filter(company -> uniqueIds.add(company.getId())).collect(Collectors.toList());
    }

    @GetMapping("/{companyId}/is-partner")
    public boolean isCompanyPartner(@PathVariable Long companyId) {
        CompanyRoleType partnerRole = companyRoleTypeRepository.findOneByCode(COMPANY_ROLETYPE_PARTNER_CODE);
        if(partnerRole == null) {
            throw new ResourceNotFoundException("Role Type PARTNER Not Found.");
        }
        List<CompanyRole> roles = companyRoleRepository.findByCompanyId(companyId);
        return roles.stream().anyMatch(role -> role.getRoleType().equals(partnerRole));
    }

    @GetMapping("/short-name")
    public String generateShortName(@RequestParam(required = false) Long companyId, @RequestParam String name) {
        return companyNameGenerator.generateShortName(companyId, name);
    }

    @Authorize(operations = "kartoteks.company.merge-with-company")
    @PutMapping("/{companyId}/merge-with/{otherCompanyId}")
    public Company saveMergedCompany(@PathVariable Long companyId, @PathVariable Long otherCompanyId, @RequestBody Company company) {
        checkCompanyIsNull(company);
        if (!companyRepository.exists(companyId)) {
            throw new ResourceNotFoundException("Company with id {0} is not found", companyId);
        }
        Company otherCompany = companyRepository.findOne(otherCompanyId);
        if (otherCompany == null) {
            throw new ResourceNotFoundException("Company with id {0} is not found", otherCompanyId);
        }
        companySaveService.mergeCompanies(company, otherCompany);
        return get(companyId);
    }

    @GetMapping("/{companyId}/download-images")
    public Company downloadImages(@PathVariable Long companyId){
        if (!companyRepository.exists(companyId)) {
            throw new ResourceNotFoundException("Company with id {0} is not found", companyId);
        }
        return companySaveService.downloadLogoImage(companyId);
    }

    @GetMapping("/download-all-images")
    public void downloadImages(){
        companySaveService.downloadAllImages();
    }

    @GetMapping("/assign-short-names")
    public void assignShortNamesForAll(){
        companySaveService.assignShortNamesFoAll();
    }

    @Authorize(operations = "kartoteks.company.edit-company")
    @PatchMapping("/{companyId}/update-short-name")
    public void updateShortName(@PathVariable Long companyId, @RequestBody UpdateShortNameRequest request){
        request.setCompanyId(companyId);
        companySaveService.updateShortNames(request);
    }

    @GetMapping("/{companyId}/verify-tax-id")
    public void verifyTaxId(@PathVariable Long companyId, @RequestParam(required = false, defaultValue = "true") Boolean bestEffortVerification) {
        verifyService.verifyTaxId(companyId, bestEffortVerification);
    }

    @GetMapping("/{companyId}/verify-eori-number")
    public void verifyEoriNumber(@PathVariable Long companyId) {
        verifyService.verifyEoriNumber(companyId);
    }
    
    @GetMapping("/list-by-email")
    public List<Company> listByContactByEmail(@RequestParam String address){
    	return companyContactRepository.findByEmailsEmailEmailAddress(address).stream().map(CompanyContact::getCompany).collect(Collectors.toList());
    }
    
    @GetMapping("/list-by-domain")
    public List<Company> listByDomain(@RequestParam String address){
    	return Stream.of(StringUtils.split(address, "@")).reduce((first,second)->second).map(domain->companyRepository.findByWebsiteEndingWithOrEmailDomain(domain, domain)).orElseGet(Collections::emptyList);
    }

}
