package ekol.location.controller;

import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ekol.event.auth.Authorize;
import ekol.exceptions.BadRequestException;
import ekol.location.domain.OperationRegion;
import ekol.location.domain.dto.DataForRegionMap;
import ekol.location.repository.OperationRegionRepository;
import ekol.location.service.OperationRegionService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/operation-region")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class OperationRegionController {

    private OperationRegionService operationRegionService;
    private OperationRegionRepository operationRegionRepository;

    @GetMapping(value = {"/query-two", "/query-two/"})
    public List<OperationRegion> findAllAccordingToQueryTwo() {
        return IterableUtils.toList(operationRegionRepository.findAllAccordingToQueryTwoDistinctByOrderByName());
    }

    @GetMapping(value = {"/query-three", "/query-three/"})
    public List<OperationRegion> findAllAccordingToQueryThree() {
        return IterableUtils.toList(operationRegionRepository.findAllAccordingToQueryThreeDistinctByOrderByName());
    }

    @GetMapping(value = "/{id}")
    public OperationRegion find(@PathVariable Long id) {
        return operationRegionService.findAccordingToQueryOneByIdOrThrowResourceNotFoundException(id);
    }

    @GetMapping(value = "/query-five/{id}")
    public OperationRegion findAccordingToQueryFive(@PathVariable Long id) {
        return operationRegionService.findAccordingToQueryFiveByIdOrThrowResourceNotFoundException(id);
    }

    @Authorize(operations="location.operation-region.manage")
    @PostMapping(value = {"", "/"})
    public OperationRegion create(@RequestBody OperationRegion operationRegion) {

        if (operationRegion != null && operationRegion.getId() != null) {
            throw new BadRequestException("This method must be used for creation.");
        }

        return operationRegionService.createOrUpdate(operationRegion);
    }

    @Authorize(operations="location.operation-region.manage")
    @PutMapping(value = {"/{id}", "/{id}/"})
    public OperationRegion update(@PathVariable Long id, @RequestBody OperationRegion operationRegion) {

        if (!id.equals(operationRegion.getId())) {
            throw new BadRequestException("OperationRegion.id must be " + id + ".");
        }

        return operationRegionService.createOrUpdate(operationRegion);
    }

    @Authorize(operations="location.operation-region.manage")
    @DeleteMapping(value = {"/{id}", "/{id}/"})
    public void delete(@PathVariable Long id) {
        operationRegionService.softDelete(id);
    }

    @GetMapping(value = "/map")
    public DataForRegionMap findDataForRegionMap(
            @RequestParam(value = "selectedOperationRegionId", required = false) Long selectedOperationRegionId) {
        return operationRegionService.findDataForOperationRegionMap(selectedOperationRegionId);
    }

    @GetMapping(value = {"/query-three/by-warehouse"})
    public OperationRegion findOperationRegionThatContainsWarehouseAccordingToQueryThree(@RequestParam(value = "warehouseId") Long warehouseId) {
        return operationRegionService.findOperationRegionThatContainsWarehouseAccordingToQueryThree(warehouseId);
    }
}
