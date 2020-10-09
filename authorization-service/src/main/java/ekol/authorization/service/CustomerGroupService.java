package ekol.authorization.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import ekol.authorization.domain.CustomerGroup;
import ekol.authorization.domain.auth.AuthCustomerGroup;
import ekol.authorization.domain.auth.AuthTeam;
import ekol.authorization.dto.CustomerGroupSearchFilter;
import ekol.authorization.dto.IdNamePair;
import ekol.authorization.repository.CustomerGroupRepository;
import ekol.authorization.repository.auth.AuthCustomerGroupRepository;
import ekol.authorization.repository.auth.AuthTeamRepository;
import ekol.exceptions.BadRequestException;
import ekol.exceptions.ResourceNotFoundException;
import ekol.exceptions.UnauthorizedAccessException;
import ekol.resource.oauth2.SessionOwner;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class CustomerGroupService {

	private AuthTeamRepository authTeamRepository;
	private CustomerGroupRepository customerGroupRepository;
	private AuthCustomerGroupRepository authCustomerGroupRepository;
	private SessionOwner sessionOwner;

	public List<CustomerGroup> findByInheritedEntity(Long inheritedEntityId) {
		Iterable<AuthCustomerGroup> authCustomerGroups = authCustomerGroupRepository.findByInheritNodeExternalId(inheritedEntityId);
		return customerGroupRepository.findAll(StreamSupport.stream(authCustomerGroups.spliterator(), true).map(AuthCustomerGroup::getExternalId).collect(Collectors.toSet()));
	}
	
	public List<CustomerGroup> findAll() {
		return IterableUtils.toList(customerGroupRepository.findAll());
	}

	public CustomerGroup findById(Long id) {
		return Optional.ofNullable(customerGroupRepository.findOne(id))
				.orElseThrow(() -> new ResourceNotFoundException("Customer Group with id {0} cannot be found.", id));
	}
	
	public AuthCustomerGroup findByExternalIdOrThrowException(Long id) {
		return Optional.ofNullable(authCustomerGroupRepository.findByExternalId(id))
				.orElseThrow(() -> new ResourceNotFoundException("Customer Group Node with external id {0} cannot be found.", id));
	}
	
	public List<CustomerGroup> search(CustomerGroupSearchFilter filter){
		return customerGroupRepository.findAll(filter.toSpecification());
	}

	@Transactional
	public CustomerGroup createOrUpdate(CustomerGroup customerGroup) {
		boolean isCreation = true;

		if (Objects.isNull(customerGroup)) {
			throw new BadRequestException("A customer group must be specified.");
		}

		if (!Objects.isNull(customerGroup.getId())) {
			findById(customerGroup.getId());
			isCreation = false;
		}

		if (StringUtils.isBlank(customerGroup.getName())) {
			throw new BadRequestException("A name must be specified.");
		}

		customerGroup.setName(StringUtils.upperCase(customerGroup.getName()));

		Optional<CustomerGroup> queryResult = customerGroupRepository.findByName(customerGroup.getName());

		if (queryResult.filter(g->!g.getId().equals(customerGroup.getId())).isPresent()) {
			throw new BadRequestException("There cannot be two customer groups with same name.");
		}

		Iterable<CustomerGroup> customerGroups = customerGroupRepository.findByCompaniesIdIn(customerGroup.getCompanies().parallelStream().map(IdNamePair::getId).collect(Collectors.toList()));
		if(StreamSupport.stream(customerGroups.spliterator(), true).anyMatch(g->!g.getId().equals(customerGroup.getId()))) {
			throw new BadRequestException("One or more companies is in another customer group");
		}

		AuthCustomerGroup graphNode = null;
		CustomerGroup persistedEntity = customerGroupRepository.save(customerGroup);
		if (isCreation) {
			graphNode = persistedEntity.toAuthCustomerGroup();
		} else {
			graphNode = findByExternalIdOrThrowException(persistedEntity.getId());
			graphNode.setName(persistedEntity.getName());
		}
		authCustomerGroupRepository.save(graphNode);
		return persistedEntity;
	}

	@Transactional
	public void delete(Long id) {
		customerGroupRepository.delete(id);
		authCustomerGroupRepository.delete(findByExternalIdOrThrowException(id));
	}

	public CustomerGroup addCompany(Long id, IdNamePair company) {
		CustomerGroup customerGroup = findById(id);
		if(customerGroup.getCompanies().parallelStream().anyMatch(c->c.getId().equals(company.getId()))) {
			throw new BadRequestException("Company is exist in this customer group");
		}
		customerGroup.getCompanies().add(company);
		return createOrUpdate(customerGroup);
	}

	public CustomerGroup deleteCompany(Long id, Long companyId) {
		CustomerGroup customerGroup = findById(id);
		Optional<IdNamePair> company = customerGroup.getCompanies().parallelStream().filter(c->c.getId().equals(companyId)).findFirst();
		if(!company.isPresent()) {
			throw new ResourceNotFoundException("Company with id {0} cannot be found.", companyId);
		}
		customerGroup.getCompanies().remove(company.get());
		return createOrUpdate(customerGroup);
	}

	public void checkAuthorizedCompany(Long companyId) {
		UnauthorizedAccessException unauthorizedAccessException = new UnauthorizedAccessException("You do not have authorization to create or update orders of the relevant customer.");
		
		CustomerGroup customerGroup = Optional.ofNullable(customerGroupRepository.findByCompaniesId(companyId))
												.orElseThrow(() -> unauthorizedAccessException);
		
		AuthCustomerGroup authCustomerGroup = findByExternalIdOrThrowException(customerGroup.getId());
		
		Supplier<Stream<Long>> groupTeamSupplier = () -> authTeamRepository.findByCustomerGroup(authCustomerGroup.getId())
															.parallelStream()
															.map(AuthTeam::getExternalId)
															.filter(Objects::nonNull);
		Supplier<Stream<Long>> userTeamSupplier = () -> sessionOwner.getCurrentUser()
															.getTeams()
															.parallelStream()
															.map(ekol.model.IdNamePair::getId)
															.filter(Objects::nonNull);
		
		if(0 == userTeamSupplier.get().flatMap(u -> groupTeamSupplier.get().filter(g -> Objects.equals(u, g))).count()) {
			throw unauthorizedAccessException;
		}
	}

	public Set<IdNamePair> listAuthorizedCompanies() {
		Set<Long> teamExternalIds = sessionOwner.getCurrentUser().getTeams().parallelStream().map(ekol.model.IdNamePair::getId).filter(Objects::nonNull).collect(Collectors.toSet());
		Iterable<AuthCustomerGroup> customerGroupNodes = authCustomerGroupRepository.findByInheritNodeExternalIdIn(teamExternalIds);
		List<CustomerGroup> customerGroups = customerGroupRepository.findAll(StreamSupport.stream(customerGroupNodes.spliterator(), true).map(AuthCustomerGroup::getExternalId).collect(Collectors.toSet()));
		return customerGroups.stream().flatMap(g->g.getCompanies().stream()).collect(Collectors.toSet());
	}

}
