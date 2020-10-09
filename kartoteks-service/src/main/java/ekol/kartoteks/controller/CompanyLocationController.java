package ekol.kartoteks.controller;

import ekol.event.auth.Authorize;
import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.kartoteks.domain.*;
import ekol.kartoteks.domain.dto.BulkExistenceAndActivenessCheckResponse;
import ekol.kartoteks.domain.dto.SetMapLocationRequest;
import ekol.kartoteks.domain.validator.CompanyLocationValidator;
import ekol.kartoteks.repository.CompanyContactRepository;
import ekol.kartoteks.repository.CompanyLocationRepository;
import ekol.kartoteks.repository.CompanyRepository;
import ekol.kartoteks.service.*;
import ekol.kartoteks.utils.ServiceUtils;
import ekol.kartoteks.service.CompanyDataExportService;
import ekol.kartoteks.service.CompanyLocationMoveService;
import ekol.kartoteks.service.CompanyNameGenerator;
import ekol.kartoteks.service.CompanySaveService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by kilimci on 10/08/16.
 */
@RestController
@RequestMapping("/location")
public class CompanyLocationController {

    @Autowired
    private CompanyLocationRepository companyLocationRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyLocationValidator companyLocationValidator;

    @Autowired
    private CompanyNameGenerator companyNameGenerator;

    @Autowired
    private CompanyLocationMoveService companyLocationMoveService;

    @Autowired
    private CompanySaveService companySaveService;

    @Autowired
    private CompanyDataExportService dataExportService;
    
    @Autowired
    private CompanyDeleteService companyDeleteService;

    @Autowired
    private CompanyContactRepository companyContactRepository;

    @RequestMapping(value="/{locationId}",method = RequestMethod.GET)
    public CompanyLocation getById(@PathVariable Long locationId){
        CompanyLocation loc = companyLocationRepository.findOne(locationId);
        if(loc == null) {
            throw new ResourceNotFoundException();
        }
        return loc;
    }

    @Authorize(operations = "kartoteks.company.edit-company")
    @RequestMapping(value="/{locationId}",method = RequestMethod.PUT)
    public CompanyLocation update(@PathVariable Long locationId, CompanyLocation companyLocation){

        if(companyLocation == null)
            throw new BadRequestException("Company Location is null");

        if(locationId!=companyLocation.getId())
            throw new BadRequestException(String.format("Company Location id doesnt equal to  %d",locationId));

        CompanyLocation l =companyLocationRepository.findOne(locationId);
        if(l == null)
            throw new ResourceNotFoundException();
        companyLocationRepository.save(companyLocation);

        return companyLocation;
    }

    @Authorize(operations = "kartoteks.company.delete-company")
    @RequestMapping(value="/{locationId}",method = RequestMethod.DELETE)
    public void delete(@PathVariable Long locationId){
        CompanyLocation loc = companyLocationRepository.findOne(locationId);
        if(loc == null)
            throw new ResourceNotFoundException();
        loc.setDeleted(true);
        companyLocationRepository.save(loc);
    }

    @RequestMapping(value = "/{locationId}/ensure-can-be-deleted", method = RequestMethod.GET)
    public void ensureLocationCanBeDeleted(@PathVariable Long locationId) {
        CompanyLocation loc = companyLocationRepository.findOne(locationId);
        if (loc == null) {
            throw new ResourceNotFoundException();
        }
        companyDeleteService.ensureLocationCanBeDeleted(loc);
    }

    @Authorize(operations = {"kartoteks.company.edit-company", "kartoteks.company.edit-temp-company", "kartoteks.company.create-company"})
    @RequestMapping(value="/validate",method = RequestMethod.PUT)
    public void validateLocation(@RequestBody CompanyLocation location){
        if(location == null) {
            throw new BadRequestException("CompanyLocation is null");
        }
        companyLocationValidator.validate(location);
    }

    @RequestMapping(value="/short-name",method = RequestMethod.GET)
    public String generateShortName(@RequestParam(required = false) Long locationId,
                                    @RequestParam String companyShortName,
                                    @RequestParam(required = false) String city,
                                    @RequestParam(required = false) String district,
                                    @RequestParam(required = false) String exclude) {
        return companyNameGenerator.generateLocationShortName(locationId, companyShortName, city, district, exclude);
    }

    @Authorize(operations = "kartoteks.company.move-location")
    @RequestMapping(value="/{locationId}/move-to/{companyId}",method = RequestMethod.POST)
    public void moveLocationTo(@PathVariable Long locationId, @PathVariable Long companyId) {
        CompanyLocation locationToMove = companyLocationRepository.findOne(locationId);
        if(locationToMove == null){
            throw new ResourceNotFoundException("Location not found");
        }
        Company company = companyRepository.findOne(companyId);
        if(company == null){
            throw new BadRequestException("Company does not exist");
        }

        companyLocationMoveService.moveTo(locationToMove, company);
    }

    @Authorize(operations = "kartoteks.company.confirm-map-location")
    @RequestMapping(value="/set-map-location", method = RequestMethod.POST)
    public void confirmMapLocation(@RequestBody SetMapLocationRequest request) {
        companySaveService.confirmPointOnMap(request.getLocationId(), request.getLat(), request.getLng());
    }

    @Authorize(operations = "kartoteks.company.export-company")
    @RequestMapping(value = "/{locationId}/export", method = RequestMethod.GET)
    public void export(@PathVariable Long locationId) {
        CompanyLocation location = companyLocationRepository.findOne(locationId);
        if (location == null) {
            throw new ResourceNotFoundException("Location not found");
        }
        dataExportService.export(location.getCompany(), location, true);
    }

    @RequestMapping(value = "/{locationId}/is-active", method = RequestMethod.GET)
    public boolean isActive(@PathVariable Long locationId) {
        if (companyLocationRepository.countByIdAndActiveTrue(locationId) > 0) {
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping(value = "/bulk-existence-and-activeness-check", method = RequestMethod.GET)
    public BulkExistenceAndActivenessCheckResponse doBulkExistenceAndActivenessCheck(@RequestParam String commaSeparatedIds) {

        List<Long> ids = ServiceUtils.commaSeparatedNumbersToLongList(commaSeparatedIds);
        Set<Long> ok = new HashSet<>(companyLocationRepository.findIdsByActiveTrueAndIdIn(ids));
        Set<Long> notOk = new HashSet<>();
        
        for (Long id : ids) {
            if (!ok.contains(id)) {
                notOk.add(id);
            }
        }
        
        return new BulkExistenceAndActivenessCheckResponse(ok, notOk);
    }

    @RequestMapping(value = "/{locationId}/contacts", method = RequestMethod.GET)
    public Set<CompanyContact> getContacts(@PathVariable Long locationId) {
        return companyContactRepository.findByCompanyLocationId(locationId);
    }

    @GetMapping("/mapped-application")
    public CompanyLocation getByMappedApplicationAndApplicationId(@RequestParam String applicationName, @RequestParam String applicationId) {
        return companyLocationRepository.findByMappedIds_applicationIsAndMappedIds_applicationLocationIdIs(RemoteApplication.valueOf(applicationName), applicationId);
    }
}
