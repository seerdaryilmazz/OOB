package ekol.crm.quote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ekol.crm.quote.domain.model.ServiceType;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, String> {
	
}
