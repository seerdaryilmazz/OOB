package ekol.location.controller;
import static java.util.stream.Collectors.toList;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.exceptions.ResourceNotFoundException;
import ekol.location.domain.*;
import ekol.location.domain.dto.*;
import ekol.location.domain.location.comnon.Place;
import ekol.location.domain.location.customerwarehouse.CustomerWarehouse;
import ekol.location.domain.location.enumeration.WarehouseOwnerType;
import ekol.location.domain.location.warehouse.Warehouse;
import ekol.location.service.*;

@RestController
@RequestMapping("/location/customs")
public class CustomsController {

    @Autowired
    private CustomerWarehouseService customerWarehouseService;

    @Autowired
    private WarehouseService warehouseService;

    @GetMapping("")
    public List<WarehouseCustomsDetailsJson> retrieveWarehouseByCustomsOfficeAndType(@RequestParam Long customsOfficeId,
                                                                                            @RequestParam WarehouseCustomsType type,
                                                                                            @RequestParam(required = false) Boolean dangerousGoods,
                                                                                            @RequestParam(required = false) Boolean tempControlledGoods,
                                                                                            @RequestParam(required = false) Boolean onBoardClearance) {
        List<WarehouseCustomsDetailsJson> result = new ArrayList<>();
        result.addAll(warehouseService.listForCustomsDetails(customsOfficeId, type).stream()
                .filter(warehouse -> dangerousGoods == null || dangerousGoods.equals(warehouse.getCustomsDetails().isUsedForDangerousGoods()))
                .filter(warehouse -> tempControlledGoods == null || tempControlledGoods.equals(warehouse.getCustomsDetails().isUsedForTempControlledGoods()))
                .filter(warehouse -> onBoardClearance == null || onBoardClearance.equals(warehouse.getCustomsDetails().isOnBoardCustomsClearance()))
                .map(warehouse -> WarehouseCustomsDetailsJson.with(
                        warehouse.getId(), warehouse.getName(), LocationType.CROSSDOCK_WAREHOUSE, warehouse.getCustomsDetails(), 
                        warehouse.getCompany(), warehouse.getCompanyLocation(), warehouse.getExternalIds(), null))
                .collect(toList()));
        result.addAll(customerWarehouseService.listForCustomsDetails(customsOfficeId, type).stream()
                .filter(warehouse -> dangerousGoods == null || dangerousGoods.equals(warehouse.getCustomsDetails().isUsedForDangerousGoods()))
                .filter(warehouse -> tempControlledGoods == null || tempControlledGoods.equals(warehouse.getCustomsDetails().isUsedForTempControlledGoods()))
                .filter(warehouse -> onBoardClearance == null || onBoardClearance.equals(warehouse.getCustomsDetails().isOnBoardCustomsClearance()))
                .map(customerWarehouse -> WarehouseCustomsDetailsJson.with(
                        customerWarehouse.getId(), customerWarehouse.getName(), LocationType.CUSTOMER_WAREHOUSE, customerWarehouse.getCustomsDetails(), 
                        customerWarehouse.getCompany(), customerWarehouse.getCompanyLocation(), customerWarehouse.getExternalIds(), customerWarehouse.getCompanyType()))
                .collect(toList()));
        return result;
    }

    @GetMapping("/{id}")
    public WarehouseCustomsDetailsJson retrieveCustomsLocationById(@PathVariable Long id){
        Warehouse warehouse = warehouseService.findByIdAndHasCustomsDetails(id);
        if(warehouse != null){
            return WarehouseCustomsDetailsJson.with(
                    warehouse.getId(), warehouse.getName(), LocationType.CROSSDOCK_WAREHOUSE, warehouse.getCustomsDetails(), 
                    warehouse.getCompany(), warehouse.getCompanyLocation(), warehouse.getExternalIds(), null);

        }
        CustomerWarehouse customerWarehouse = customerWarehouseService.findByIdAndHasCustomsDetails(id);
        if(customerWarehouse != null){
            return WarehouseCustomsDetailsJson.with(
                    customerWarehouse.getId(), customerWarehouse.getName(), LocationType.CUSTOMER_WAREHOUSE, customerWarehouse.getCustomsDetails(), 
                    customerWarehouse.getCompany(), customerWarehouse.getCompanyLocation(), customerWarehouse.getExternalIds(), customerWarehouse.getCompanyType());
        }
        throw new ResourceNotFoundException("There is no customs location with id {0}", id);
    }

    @GetMapping("/by-location/{id}")
    public WarehouseCustomsDetailsJson retrieveCustomsLocationByLocationId(@PathVariable Long id, @RequestParam(required=false, defaultValue="COMPANY") WarehouseCompanyType companyType ){
    	if(WarehouseCompanyType.COMPANY == companyType) {
    		Warehouse warehouse = warehouseService.findByLocationIdAndHasCustomsDetails(id);
    		if(warehouse != null){
    			return WarehouseCustomsDetailsJson.with(
    					warehouse.getId(), warehouse.getName(), LocationType.CROSSDOCK_WAREHOUSE, warehouse.getCustomsDetails(), 
    					warehouse.getCompany(), warehouse.getCompanyLocation(), warehouse.getExternalIds(), null);

    		}
    	}
        CustomerWarehouse customerWarehouse = customerWarehouseService.findByLocationIdAndHasCustomsDetails(id, companyType);
        if(customerWarehouse != null){
            return WarehouseCustomsDetailsJson.with(
                    customerWarehouse.getId(), customerWarehouse.getName(), LocationType.CUSTOMER_WAREHOUSE, customerWarehouse.getCustomsDetails(), 
                    customerWarehouse.getCompany(), customerWarehouse.getCompanyLocation(), customerWarehouse.getExternalIds(), customerWarehouse.getCompanyType());
        }

        return null;
    }

    @GetMapping("/european-customs-companies")
    public List<IdTypeName> retrieveCustomsWarehouseCompanies(){
        List<IdTypeName> result = new ArrayList<>();
        result.addAll(warehouseService.findByCustomsDetailsCustomsType(WarehouseCustomsType.EUROPE_CUSTOMS_LOCATION).stream()
                .map(warehouse -> IdTypeName.with(warehouse.getCompany().getId(), WarehouseCompanyType.COMPANY.name(), warehouse.getCompany().getName()))
                .distinct()
                .collect(toList()));

        result.addAll(customerWarehouseService.findByCustomsDetailsCustomsType(WarehouseCustomsType.EUROPE_CUSTOMS_LOCATION).stream()
                .map(warehouse -> IdTypeName.with(warehouse.getCompany().getId(), warehouse.getCompanyType().name(), warehouse.getCompany().getName()))
                .distinct()
                .collect(toList()));

        return result;
    }


    @GetMapping("/european-customs")
    public List<WarehouseCustomsDetailsJson> retrieveWarehouseByCompany(@RequestParam Long companyId, @RequestParam(required=false, defaultValue="COMPANY") WarehouseCompanyType companyType ){
        List<WarehouseCustomsDetailsJson> result = new ArrayList<>();
        if(WarehouseCompanyType.COMPANY == companyType) {
	        result.addAll(warehouseService.listForCompany(companyId).stream()
	                .filter(warehouse -> warehouse.getCustomsDetails() != null &&
	                        StringUtils.isNotBlank(warehouse.getCustomsDetails().getEuropeanCustomsCode()))
	                .map(warehouse -> WarehouseCustomsDetailsJson.with(warehouse.getId(), warehouse.getName(),
	                        LocationType.CROSSDOCK_WAREHOUSE, warehouse.getCustomsDetails(),
	                		warehouse.getCompany(), warehouse.getCompanyLocation(), warehouse.getExternalIds(), null)).collect(toList()));
        }

        result.addAll(customerWarehouseService.listForCompany(companyId).stream().filter(e->Objects.equals(e.getCompanyType(),companyType))
                .filter(customerWarehouse -> customerWarehouse.getCustomsDetails() != null &&
                        StringUtils.isNotBlank(customerWarehouse.getCustomsDetails().getEuropeanCustomsCode()))
                .map(customerWarehouse -> WarehouseCustomsDetailsJson.with(customerWarehouse.getId(), customerWarehouse.getName(),
                        LocationType.CUSTOMER_WAREHOUSE, customerWarehouse.getCustomsDetails(),
                        customerWarehouse.getCompany(), customerWarehouse.getCompanyLocation(), customerWarehouse.getExternalIds(), customerWarehouse.getCompanyType())).collect(toList()));
        return result;
    }

    @GetMapping("/warehouses")
    public List<WarehouseCompanyLocationDetailsJson> search(@RequestParam(required = false) WarehouseOwnerType ownerType,
                                                            @RequestParam(required = false) WarehouseCustomsType customsType,
                                                            @RequestParam(required = false) WarehouseCompanyType companyType,
                                                            @RequestParam(required = false) Long customsOfficeId,
                                                            @RequestParam(required = false) String countryCode,
                                                            @RequestParam(required = false) String postalCode){
        List<WarehouseCompanyLocationDetailsJson> result = new ArrayList<>();
        result.addAll(
                warehouseService.search(ownerType, customsType, customsOfficeId, countryCode, postalCode).stream()
                        .map(warehouse -> WarehouseCompanyLocationDetailsJson
                                .with(warehouse.getCompany(), warehouse.getCompanyLocation(), warehouse.getEstablishment()))
                        .collect(toList())
        );
        if(Objects.isNull(ownerType) || Objects.nonNull(companyType)){
            result.addAll(
                    customerWarehouseService.search(companyType, customsType, customsOfficeId, countryCode, postalCode).stream()
                            .map(warehouse -> WarehouseCompanyLocationDetailsJson
                                    .with(warehouse.getCompany(), warehouse.getCompanyLocation(), warehouse.getEstablishment()))
                            .collect(toList())
            );
        }
        return result;
    }
    
    @GetMapping("/place/{locationId}")
    public Place getPlaceByLocationId(@PathVariable Long locationId, @RequestParam(required=false, defaultValue="COMPANY") WarehouseCompanyType companyType) {
    	Optional<Place> place = Optional.ofNullable(customerWarehouseService.retrieveCustomerWarehouseByCompanyAndLocation(locationId, companyType));
		return place.orElse(warehouseService.getWarehouseByCompanyLocation(locationId));
    }
}
