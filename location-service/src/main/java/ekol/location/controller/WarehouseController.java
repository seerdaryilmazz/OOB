package ekol.location.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import ekol.event.auth.Authorize;
import ekol.location.domain.WarehouseCustomsType;
import ekol.location.domain.dto.SearchWarehouseJson;
import ekol.location.domain.location.comnon.IdNameEmbeddable;
import ekol.location.domain.location.warehouse.Warehouse;
import ekol.location.repository.WarehouseRepository;
import ekol.location.service.WarehouseService;
import ekol.location.util.ServiceUtils;
import lombok.AllArgsConstructor;

/**
 * Created by burak on 04/05/17.
 */
@RestController
@RequestMapping("/location/warehouse")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class WarehouseController {

    private WarehouseService warehouseService;

    private WarehouseRepository warehouseRepository;

    @GetMapping(value = {""})
    public List<Warehouse> retrieveWarehouses() {
        return  warehouseService.listWarehouses();
    }

    @GetMapping(value = {"/{warehouseId}"})
    public Warehouse getWarehouse(@PathVariable Long warehouseId) {
        return  warehouseService.getWarehouse(warehouseId);
    }

    @GetMapping(value = {"/bycompanylocation/{companyLocationId}"})
    public Warehouse getWarehouseByCompanyLocationId(@PathVariable Long companyLocationId) {
        return  warehouseService.getWarehouseByCompanyLocation(companyLocationId);
    }

    @GetMapping(value = {"/existsbycompanylocation/{companyLocationIds}"})
    public boolean existsByCompanyLocationIds(@PathVariable String companyLocationIds) {
        if (warehouseRepository.countByCompanyLocationIdIn(ServiceUtils.commaSeparatedNumbersToLongList(companyLocationIds)) > 0) {
            return true;
        } else {
            return false;
        }
    }

    @GetMapping(value = {"/idnamepair"})
    public List<IdNameEmbeddable> retrieveWarehousesNameIdPair() {
        return  warehouseService.listWarehousesIdNamePair();
    }

    @Authorize(operations="location.warehouse.manage")
    @PostMapping(value = {""})
    public Warehouse addWarehouse(@RequestBody Warehouse warehouse) {
        return  warehouseService.addWarehouse(warehouse);
    }

    @Authorize(operations="location.warehouse.manage")
    @PutMapping(value = {"/{warehouseId}"})
    public Warehouse editWarehouse(@PathVariable Long warehouseId, @RequestBody Warehouse warehouse) {
        return  warehouseService.editWarehouse(warehouseId, warehouse);
    }

    @Authorize(operations="location.warehouse.manage")
    @DeleteMapping(value = {"/{warehouseId}"})
    public void deleteWarehouse(@PathVariable Long warehouseId) {
        warehouseService.deleteWarehouse(warehouseId);
    }

    @PostMapping(value = {"/validate/zone"})
    public void validateZone(@RequestBody Warehouse warehouse) {
        warehouseService.validateZone(warehouse);
    }


    @PostMapping(value = {"/ramp/listtoberemoved"})
    public Set<Integer> simulateRamp(@RequestBody Warehouse warehouse) {
        return warehouseService.listRampsToBeRemoved(warehouse);
    }

    @PostMapping(value = {"/validate/ramp"})
    public void validateRamp(@RequestBody Warehouse warehouse) {
        warehouseService.validateRamp(warehouse);
    }

    @GetMapping(value = "/bycustoms")
    public List<Warehouse> retrieveWarehouseByCompanyAndLocationIdAndType(@RequestParam Long customsOfficeId,
                                                                          @RequestParam WarehouseCustomsType type) {
        return warehouseService.listForCustomsDetails(customsOfficeId, type);
    }
    
    @PostMapping("/search")
    public Page<Warehouse> searchWarehouse(@RequestBody SearchWarehouseJson terms) {
    	return warehouseService.searchWarehouse(terms);
    }
    
    @PostMapping("/query")
    public List<Warehouse> listWarehouse(@RequestBody SearchWarehouseJson terms) {
    	return warehouseService.list(terms);
    }
}
