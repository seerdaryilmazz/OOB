package ekol.crm.quote.repository;

import org.springframework.data.jpa.repository.*;

import ekol.crm.quote.domain.model.PriceAuthorization;
import ekol.hibernate5.domain.repository.ApplicationRepository;

public interface PriceAuthorizationRepository extends ApplicationRepository<PriceAuthorization> {
	
	PriceAuthorization findByPriceId(Long priceId);
	
	@Modifying
	@Query("update #{#entityName} set deleted=1, deletedAt=current_timestamp where id=?1")
	void delete(Long id);

	@Modifying
	@Query("update #{#entityName} set deleted=1, deletedAt=current_timestamp where price_id=?1 and deleted = 0")
	void deleteByPriceId(Long priceId);

	@Modifying
	@Query("update #{#entityName} set deleted=1, deletedAt=current_timestamp")
	void deleteAll();
	
	default void delete(PriceAuthorization entity) {
		delete(entity.getId());
	}
}
