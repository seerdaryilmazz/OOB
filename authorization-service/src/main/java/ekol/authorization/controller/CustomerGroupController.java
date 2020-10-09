package ekol.authorization.controller;

import java.util.List;
import java.util.Set;

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

import ekol.authorization.domain.CustomerGroup;
import ekol.authorization.dto.CustomerGroupSearchFilter;
import ekol.authorization.dto.IdNamePair;
import ekol.authorization.service.CustomerGroupService;
import ekol.event.auth.Authorize;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/customer-group")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class CustomerGroupController {

	private CustomerGroupService customerGroupService;

	@GetMapping
	public List<CustomerGroup> findAll() {
		return customerGroupService.findAll();
	}

	@GetMapping("/by-inherited/{inheritedEntityId}")
	public List<CustomerGroup> findByTeam(@PathVariable Long inheritedEntityId) {
		return customerGroupService.findByInheritedEntity(inheritedEntityId);
	}

	@GetMapping(path = { "/{id}", "/{id}/" })
	public CustomerGroup findById(@PathVariable Long id) {
		return customerGroupService.findById(id);
	}

	@Authorize(operations="customer-portfolio.manage")
	@PostMapping
	public CustomerGroup create(@RequestBody CustomerGroup customerGroup) {
		return customerGroupService.createOrUpdate(customerGroup);
	}

	@Authorize(operations="customer-portfolio.manage")
	@PutMapping(path = { "/{id}", "/{id}/" })
	public CustomerGroup update(@PathVariable Long id, @RequestBody CustomerGroup customerGroup) {
		return customerGroupService.createOrUpdate(customerGroup);
	}

	@Authorize(operations="customer-portfolio.manage")
	@DeleteMapping(path = { "/{id}", "/{id}/" })
	public void delete(@PathVariable Long id) {
		customerGroupService.delete(id);
	}

	@GetMapping(path = "/search")
	public List<CustomerGroup> search(@RequestParam(required = false) String groupName, @RequestParam(required = false) String companyName) {
		return customerGroupService.search(CustomerGroupSearchFilter.builder().groupName(groupName).companyName(companyName).build());
	}

	@Authorize(operations="customer-portfolio.manage")
	@PutMapping(path = "/{id}/company")
	public CustomerGroup addCompany(@PathVariable Long id, @RequestBody IdNamePair company) {
		return customerGroupService.addCompany(id, company);
	}

	@Authorize(operations="customer-portfolio.manage")
	@DeleteMapping(path = "/{id}/company/{companyId}")
	public CustomerGroup addCompany(@PathVariable Long id, @PathVariable Long companyId) {
		return customerGroupService.deleteCompany(id, companyId);
	}
	
	@GetMapping(path = "/authorize/company")
	public Set<IdNamePair> listAuthorizedCompanies(){
		return customerGroupService.listAuthorizedCompanies();
	}

	@GetMapping(path = "/authorize/company/{id}")
	public void checkAuthorizedCompany(@PathVariable Long id) {
		customerGroupService.checkAuthorizedCompany(id);
	}
}
