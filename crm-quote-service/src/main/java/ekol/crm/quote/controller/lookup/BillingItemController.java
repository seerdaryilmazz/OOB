package ekol.crm.quote.controller.lookup;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.crm.quote.domain.model.BillingItem;
import ekol.crm.quote.service.BillingItemService;
import ekol.model.CodeNamePair;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/lookup/billing-item")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BillingItemController {

    private BillingItemService billItemService;
    
    @PostConstruct
	@GetMapping("/clear-cache")
	public String clearCache() {
    	return billItemService.clearCache();
	}
    
    @GetMapping
    public Iterable<BillingItem> list(){
    	return billItemService.listBillingItems();
    }
    
    @GetMapping("/{id}")
    public BillingItem findById(@PathVariable Long id){
    	return billItemService.findById(id);
    }
    
    @PostMapping
    public BillingItem save(@RequestBody BillingItem request){
    	return billItemService.save(request);
    }
    
    @PutMapping("/{id}")
    public BillingItem update(@PathVariable Long id, @RequestBody BillingItem request){
    	return billItemService.update(id, request);
    }

    @GetMapping("/serviceArea/{serviceArea}")
    public List<BillingItem> list(@PathVariable String serviceArea) {
    	return billItemService.getBillingItemsByServiceArea(serviceArea);
    }

    @PostMapping("/serviceAreas")
    public List<BillingItem> getBillingItemsByServiceAreas(@RequestBody List<CodeNamePair> serviceAreas) {
    	return billItemService.getBillingItemsByServiceAreas(serviceAreas.stream().map(CodeNamePair::getCode).collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    @GetMapping("/by")
    public BillingItem getByName(@RequestParam String name){
    	return billItemService.getBillingItemByName(name);
    }

}

