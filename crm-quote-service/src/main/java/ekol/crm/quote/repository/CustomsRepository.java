package ekol.crm.quote.repository;

import ekol.crm.quote.domain.model.Customs;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.Optional;

public interface CustomsRepository extends ApplicationRepository<Customs> {

    Optional<Customs> findById(Long id);
}
