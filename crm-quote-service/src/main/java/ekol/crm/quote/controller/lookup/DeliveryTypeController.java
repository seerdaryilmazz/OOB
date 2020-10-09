package ekol.crm.quote.controller.lookup;

import java.util.List;
import java.util.stream.*;

import org.springframework.web.bind.annotation.*;

import ekol.crm.quote.domain.enumaration.DeliveryType;


@RestController
@RequestMapping("/lookup/delivery-type")
public class DeliveryTypeController {

	
    @GetMapping
    public List<DeliveryType> list(
    		@RequestParam(required = false, defaultValue = "") String operation, 
    		@RequestParam String serviceArea){
        return Stream.of(DeliveryType.values())
        		.filter(t->Stream.of(t.getServiceArea()).anyMatch(serviceArea::equalsIgnoreCase))
        		.filter(t -> null == t.getOperation() || Stream.of(t.getOperation()).anyMatch(operation::equalsIgnoreCase))
        		.collect(Collectors.toList());
    }
}
