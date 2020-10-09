package ekol.crm.quote.controller.lookup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.quote.domain.enumaration.ServiceTypeCategory;
import ekol.crm.quote.domain.model.ServiceType;
import ekol.crm.quote.service.ServiceTypeService;
import ekol.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/lookup/service-type")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ServiceTypeController{

	private ServiceTypeService serviceTypeService;
	
    @GetMapping
    public List<ServiceType> list(
    		@RequestParam(required = false) ServiceTypeCategory category, 
    		@RequestParam(required = false) String[] serviceArea) {
    	return serviceTypeService.listBy(null, category, serviceArea);
    }
    
    @GetMapping("/by-code")
    public ServiceType list(@RequestParam String code) {
    	return serviceTypeService.listBy(code, null, null).stream().findFirst().orElseThrow(ResourceNotFoundException::new);
    }
    
    @GetMapping("/{id}")
    public ServiceType get(@PathVariable String id) {
    	return serviceTypeService.findById(id);
    }
    
    @PostMapping
    public ServiceType save(@RequestBody ServiceType request) {
    	return serviceTypeService.save(request);
    }
    
    @PutMapping("/{id}")
    public ServiceType update(@PathVariable String id, @RequestBody ServiceType request) {
    	return serviceTypeService.update(id, request);
    }
    
    @GetMapping("/extra-service")
    public List<ServiceType> list(
    		@RequestParam String serviceArea,
    		@RequestParam Long subsidiaryId) {
    	return serviceTypeService.listExtraServices(serviceArea, subsidiaryId);
    }
}
