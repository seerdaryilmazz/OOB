package ekol.location.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import ekol.exceptions.*;
import ekol.location.domain.WarehouseCustomsType;
import ekol.location.domain.dto.SearchWarehouseJson;
import ekol.location.domain.location.comnon.*;
import ekol.location.domain.location.customs.CustomsOffice;
import ekol.location.domain.location.enumeration.WarehouseOwnerType;
import ekol.location.domain.location.warehouse.*;
import ekol.location.repository.*;
import ekol.location.repository.specs.WarehouseSpecification;
import ekol.location.util.WarehouseRampManager;
import ekol.location.validator.*;

/**
 * Created by burak on 09/02/17.
 */
@Service
public class WarehouseService {


    @Autowired
    private WarehouseZoneService warehouseZoneService;

    @Autowired
    private WarehouseRampManager warehouseRampManager;

    @Autowired
    private WarehouseRampRepository warehouseRampRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private EstablishmentService establishmentService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CustomsDetailsRepository customsDetailsRepository;

    @Autowired
    private LinehaulRouteLegStopService linehaulRouteLegStopService;

    @Autowired
    private CustomsOfficeService customsOfficeService;

    public List<Warehouse> listWarehouses() {
        List<Warehouse> warehouses = (List<Warehouse>)warehouseRepository.findAll();

        return warehouses.stream().map( w -> warehouseRampManager.groupRamps(w)).collect(Collectors.toList());
    }

    public Warehouse getWarehouse(Long id) {
        return warehouseRampManager.groupRamps(warehouseRepository.findOne(id));
    }

    public Warehouse getWarehouseByCompanyLocation(Long companyLocationId) {
        return warehouseRepository.findByCompanyLocationId(companyLocationId);
    }

    public List<IdNameEmbeddable> listWarehousesIdNamePair() {

        List<IdNameEmbeddable> result = new ArrayList<>();

        listWarehouses().forEach(elem -> {
            result.add(new IdNameEmbeddable(elem.getId(), elem.getName()));
        });

        return result;
    }

    public Warehouse addWarehouse(Warehouse warehouse) {

        WarehouseValidator.validate(warehouse);

        warehouse.setEstablishment(establishmentService.save(warehouse.getEstablishment()));
        warehouse.getLocation().setTimezone(warehouse.getCountry().getTimezone());
        warehouse.setLocation(locationRepository.save(warehouse.getLocation()));
        warehouse.setRouteLegStop(linehaulRouteLegStopService.createOrUpdate(warehouse.buildRouteLegStop()));
        updateCustomsDetailsWithCustomsType(warehouse);
        if(warehouse.getCustomsDetails() != null){
            warehouse.setCustomsDetails(customsDetailsRepository.save(warehouse.getCustomsDetails()));
        }

        Warehouse saved = warehouseRepository.save(warehouse);

        Collection<WarehouseRamp> ramps = warehouseRampManager.prepareRampEntries(warehouse.getRampGroup(), saved.getRamp());
        warehouseRampRepository.save(ramps);

        warehouseZoneService.handleWarehouseZones(saved, warehouse.getZone(), ramps);

        return saved;
    }


    public Warehouse editWarehouse(Long warehouseId, Warehouse warehouse) {
        if (warehouseId == null) {
            throw new BadRequestException("Warehouse id is null");
        }

        WarehouseValidator.validate(warehouse);

        Warehouse fromDB = warehouseRepository.findOne(warehouseId);

        if (fromDB == null) {
            throw new ResourceNotFoundException("Warehouse with given id '" + warehouseId + "' is not found");
        }

        fromDB.setName(warehouse.getName());
        fromDB.setArea(warehouse.getArea());
        fromDB.setStorageVolume(warehouse.getStorageVolume());
        fromDB.setNumberOfFloors(warehouse.getNumberOfFloors());
        fromDB.setNumberOfRamps(warehouse.getNumberOfRamps());
        fromDB.setExternalIds(warehouse.getExternalIds());

        fromDB.setEstablishment(establishmentService.save(warehouse.getEstablishment()));
        //location is readonly but polygon is editable
        Set<PolygonPoint> polygonPath = warehouse.getLocation() != null ? warehouse.getLocation().getPolygonPath() : null;
        fromDB.getLocation().setPolygonPath(polygonPath);
        warehouse.setLocation(locationRepository.save(fromDB.getLocation()));
        warehouse.setRouteLegStop(linehaulRouteLegStopService.createOrUpdate(warehouse.buildRouteLegStop()));
        updateCustomsDetailsWithCustomsType(warehouse);
        if(warehouse.getCustomsDetails() != null){
            fromDB.setCustomsDetails(customsDetailsRepository.save(warehouse.getCustomsDetails()));
        }else{
            fromDB.setCustomsDetails(null);
        }

        Warehouse savedWh = warehouseRepository.save(fromDB);

        Collection<WarehouseRamp> ramps = warehouseRampManager.prepareRampEntries(warehouse.getRampGroup(), savedWh.getRamp());
        warehouseRampRepository.save(ramps);

        warehouseZoneService.handleWarehouseZones(savedWh, warehouse.getZone(), ramps);

        return savedWh;
    }

    public void deleteWarehouse(Long warehouseId) {
    	Warehouse warehouse = findByIdOrThrowResourceNotFoundException(warehouseId);
    	warehouse.setDeleted(true);
    	Optional.ofNullable(warehouse.getRouteLegStop()).ifPresent(t->t.setDeleted(true));
    	warehouseRepository.save(warehouse);
    }

    public Set<Integer> listRampsToBeRemoved(Warehouse warehouse) {
      return new HashSet<>();
    }

    public void validateZone(Warehouse warehouse) {
        WarehouseZoneValidator.validate(warehouse);
    }

    public void validateRamp(Warehouse warehouse) {
        WarehouseRampValidator.validate(warehouse);
    }

    public Warehouse findByIdOrThrowResourceNotFoundException(Long id) {
    	return Optional.ofNullable(warehouseRepository.findOne(id)).orElseThrow(()->new ResourceNotFoundException("Warehouse with specified id cannot be found: " + id));
    }

    private void updateCustomsDetailsWithCustomsType(Warehouse warehouse){
        if(warehouse.getCustomsDetails() != null){
            if(warehouse.getCustomsDetails().getCustomsType().equals(WarehouseCustomsType.NON_BONDED_WAREHOUSE)){
                warehouse.setCustomsDetails(null);
            }else if(warehouse.getCustomsDetails().getCustomsType().equals(WarehouseCustomsType.EUROPE_CUSTOMS_LOCATION)){
                warehouse.getCustomsDetails().setUsedForDangerousGoods(true);
                warehouse.getCustomsDetails().setUsedForTempControlledGoods(true);
            }
        }
    }

    public List<Warehouse> listForCustomsDetails(Long customsOfficeId, WarehouseCustomsType customsType){
        CustomsOffice customsOffice = customsOfficeService.getOrThrowException(customsOfficeId);
        return warehouseRepository.findByCustomsDetailsCustomsOfficeAndCustomsDetailsCustomsType(customsOffice, customsType);
    }

    public List<Warehouse> listForCompany(Long companyId){
        return warehouseRepository.findByCompanyId(companyId);
    }

    public Warehouse findByIdAndHasCustomsDetails(Long id){
        return warehouseRepository.findByIdAndCustomsDetailsNotNull(id).orElse(null);
    }

    public Warehouse findByLocationIdAndHasCustomsDetails(Long id){
        return warehouseRepository.findByCompanyLocationIdAndCustomsDetailsNotNull(id).orElse(null);
    }

    public List<Warehouse> findByCustomsDetailsCustomsType(WarehouseCustomsType customsType){
        return warehouseRepository.findByCustomsDetailsCustomsType(customsType);
    }

    public List<Warehouse> search(WarehouseOwnerType ownerType, WarehouseCustomsType customsType, Long customsOfficeId, String countryCode, String postalCode){
        Specifications<Warehouse> specs = null;
        if(ownerType != null){
            specs = WarehouseSpecification.append(specs, WarehouseSpecification.havingOwnerType(ownerType));
        }
        if(customsType != null){
            specs = WarehouseSpecification.append(specs, WarehouseSpecification.havingCustomsDetailsCustomsType(customsType));
        }
        if(customsOfficeId != null){
            specs = WarehouseSpecification.append(specs, WarehouseSpecification.havingCustomsDetailsCustomsOffice(customsOfficeId));
        }
        if(countryCode != null){
            specs = WarehouseSpecification.append(specs, WarehouseSpecification.havingAdressCountryCode(countryCode));
        }
        if(postalCode != null){
            specs = WarehouseSpecification.append(specs, WarehouseSpecification.havingAdressPostalCode(postalCode));
        }
        return warehouseRepository.findAll(specs);

    }
    
    public Page<Warehouse> searchWarehouse(SearchWarehouseJson terms) {
    	return warehouseRepository.findAll(terms.toSpecification(), new PageRequest(terms.getPage(), terms.getPageSize()));
    }
    
    public List<Warehouse> list(SearchWarehouseJson terms) {
    	return warehouseRepository.findAll(terms.toSpecification());
    }
}
