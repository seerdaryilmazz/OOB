package ekol.orders.lookup.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.event.auth.Authorize;
import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.orders.lookup.domain.Incoterm;
import ekol.orders.lookup.repository.IncotermRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/lookup/incoterm")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class IncotermLookupController extends BaseLookupApiController<Incoterm> {

    private IncotermRepository incotermRepository;

    @PostConstruct
    public void init(){
        setLookupRepository(incotermRepository);
    }
    
    @Override
    @Authorize(operations="order.incoterms.manage")
    public Incoterm add(@RequestBody Incoterm lookup) {
    	return super.add(lookup);
    }
    
    @Override
    @Authorize(operations="order.incoterms.manage")
    public Incoterm update(@PathVariable Long id, @RequestBody Incoterm updatedLookup) {
    	return super.update(id, updatedLookup);
    }
    
    @Override
    @Authorize(operations="order.incoterms.manage")
    public void delete(@PathVariable Long id) {
    	super.delete(id);
    }
}
