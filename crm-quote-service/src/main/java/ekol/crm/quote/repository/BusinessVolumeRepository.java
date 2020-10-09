package ekol.crm.quote.repository;

import java.util.Optional;

import ekol.crm.quote.domain.model.businessVolume.BusinessVolume;
import ekol.hibernate5.domain.repository.ApplicationRepository;

public interface BusinessVolumeRepository extends ApplicationRepository<BusinessVolume>{
	
	Optional<BusinessVolume> findById(Long id);

}
