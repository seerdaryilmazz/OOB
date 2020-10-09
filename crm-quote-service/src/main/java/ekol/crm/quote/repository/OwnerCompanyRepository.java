package ekol.crm.quote.repository;

import java.util.List;

import ekol.crm.quote.domain.model.OwnerCompany;
import ekol.hibernate5.domain.repository.ApplicationRepository;

public interface OwnerCompanyRepository extends ApplicationRepository<OwnerCompany>{
	
	List<OwnerCompany> findByType(String type);

}
