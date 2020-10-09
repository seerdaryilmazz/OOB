package ekol.orders.lookup.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ekol.event.auth.Authorize;
import ekol.hibernate5.domain.controller.BaseController;
import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.orders.lookup.domain.HSCodeExtended;
import ekol.orders.lookup.dto.HSCodeSearchJson;
import ekol.orders.lookup.repository.HSCodeExtendedRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/lookup/hscode-extended")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class HSCodeExtendedController extends BaseController<HSCodeExtended> {
	
    private HSCodeExtendedRepository hsCodeExtendedRepository;

	@Override
	protected ApplicationRepository<HSCodeExtended> getApplicationRepository() {
		return hsCodeExtendedRepository;
	}
	
    @GetMapping(value="/search")
    public Page<HSCodeExtended> search(HSCodeSearchJson searchTerm) {
    	return hsCodeExtendedRepository.findAll(searchTerm.toSpecification(), new PageRequest(searchTerm.getPage(), searchTerm.getPageSize()));
    }

    @GetMapping(value="/search-by-id")
    public Set<HSCodeExtended> search(@RequestParam List<Long> id){
    	return StreamSupport.stream(hsCodeExtendedRepository.findAll(id).spliterator(),true).collect(Collectors.toSet());
    }

    @Override
    @Authorize(operations="order.hscode.manage")
    public HSCodeExtended add(@RequestBody HSCodeExtended entity) {
    	return super.add(entity);
    }
    
    @Override
    @Authorize(operations="order.hscode.manage")
    public HSCodeExtended update(@PathVariable Long id, @RequestBody HSCodeExtended entity) {
    	return super.update(id, entity);
    }

    @Override
    @Authorize(operations="order.hscode.manage")
    public void delete(@PathVariable Long id) {
    	super.delete(id);
    }
}
