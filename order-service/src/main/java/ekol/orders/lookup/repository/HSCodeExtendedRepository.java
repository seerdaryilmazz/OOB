package ekol.orders.lookup.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import ekol.hibernate5.domain.repository.LookupRepository;
import ekol.orders.lookup.domain.HSCodeExtended;

public interface HSCodeExtendedRepository extends LookupRepository<HSCodeExtended>, JpaSpecificationExecutor<HSCodeExtended> {
	HSCodeExtended findByName(String name);
}
