package ekol.location.controller;

import ekol.event.auth.Authorize;
import ekol.location.domain.WarehouseCompanyType;
import ekol.location.domain.WarehouseCustomsType;
import ekol.location.domain.dto.SearchWarehouseJson;
import ekol.location.domain.dto.WarehouseCustomsDetailsJson;
import ekol.location.domain.location.customerwarehouse.CustomerWarehouse;
import ekol.location.repository.CustomerWarehouseRepository;
import ekol.location.service.CustomerWarehouseService;
import ekol.location.util.ServiceUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by burak on 11/04/17.
 */
@RestController
@RequestMapping("/location/customerwarehouse")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class CustomerWarehouseController {

    private CustomerWarehouseService customerWarehouseService;
    
    private CustomerWarehouseRepository customerWarehouseRepository;

    @Authorize(operations="location.customer-warehouse.manage")
    @PostMapping(value = "")
    public CustomerWarehouse addCustomerWarehouse(@RequestBody CustomerWarehouse customerWarehouse) {
        customerWarehouseService.addCustomerWarehouse(customerWarehouse);
        return customerWarehouseService.retrieveCustomerWarehouseById(customerWarehouse.getId());    }

    @Authorize(operations="location.customer-warehouse.manage")
    @PutMapping(value = "/{customerWarehouseId}")
    public CustomerWarehouse editCustomerWarehouse(@PathVariable Long customerWarehouseId, @RequestBody CustomerWarehouse customerWarehouse) {
        customerWarehouseService.editCustomerWarehouse(customerWarehouseId, customerWarehouse);
        return customerWarehouseService.retrieveCustomerWarehouseById(customerWarehouse.getId());
    }

    @GetMapping(value = "")
    public Iterable<CustomerWarehouse> retrieveCustomerWarehouse() {
        return customerWarehouseService.retrieveCustomerWarehouses();
    }

    @GetMapping(value = "/{customerWarehouseId}")
    public CustomerWarehouse retrieveCustomerWarehouseById(@PathVariable Long customerWarehouseId) {
        return customerWarehouseService.retrieveCustomerWarehouseById(customerWarehouseId);
    }

    @Authorize(operations="location.customer-warehouse.manage")
    @DeleteMapping(value = "/{customerWarehouseId}")
    public void deleteCustomerWarehouse(@PathVariable Long customerWarehouseId) {
        customerWarehouseService.delete(customerWarehouseId);
    }

    @GetMapping(value = "/bycompanylocation/{companyLocationId}")
    public CustomerWarehouse retrieveCustomerWarehouseByCompanyAndLocationId(@PathVariable Long companyLocationId) {
        return customerWarehouseService.retrieveCustomerWarehouseByCompanyAndLocation(companyLocationId);
    }

    @GetMapping(value = "/bycompany/{companyId}")
    public List<WarehouseCustomsDetailsJson> retrieveCustomerWarehouseByCompanyId(@PathVariable Long companyId) {
        return customerWarehouseService.retrieveCustomerWarehouseByCompany(companyId).stream()
                .map(WarehouseCustomsDetailsJson::with).collect(toList());
    }

    @GetMapping(value = {"/existsbycompanylocation/{companyLocationIds}"})
    public boolean existsByCompanyLocationIds(@PathVariable String companyLocationIds) {
        if (customerWarehouseRepository.countByCompanyLocationIdIn(ServiceUtils.commaSeparatedNumbersToLongList(companyLocationIds)) > 0) {
            return true;
        } else {
            return false;
        }
    }

    @GetMapping(value = "/bycompanylocation/{type}/{companyLocationId}")
    public CustomerWarehouse retrieveCustomerWarehouseByCompanyAndLocationIdAndType(@PathVariable Long companyLocationId,
                                                                                    @PathVariable WarehouseCompanyType type) {
        return customerWarehouseService.retrieveCustomerWarehouseByCompanyAndLocation(companyLocationId, type);
    }

    @GetMapping(value = "/bycustoms")
    public List<CustomerWarehouse> retrieveCustomerWarehouseByCompanyAndLocationIdAndType(@RequestParam Long customsOfficeId,
                                                                                          @RequestParam WarehouseCustomsType type) {
        return customerWarehouseService.listForCustomsDetails(customsOfficeId, type);
    }
    
    @PostMapping("/search")
    public Page<CustomerWarehouse> searchWarehouse(@RequestBody SearchWarehouseJson terms) {
    	return customerWarehouseService.searchcustomerWarehouse(terms);
    }

}

