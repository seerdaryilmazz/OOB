package ekol.authorization.repository;import java.util.Optional;

import ekol.authorization.domain.CustomerGroup;

public interface CustomerGroupRepository extends ApplicationRepository<CustomerGroup> {
	
	Optional<CustomerGroup> findByName(String name);
	
	Iterable<CustomerGroup> findByCompaniesIdIn(Iterable<Long> companies);

	CustomerGroup findByCompaniesId(Long companyId);
}
