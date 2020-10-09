package ekol.authorization.repository;

import ekol.authorization.domain.Subsidiary;
import ekol.hibernate5.domain.repository.ApplicationRepository;

public interface SubsidiaryRepository extends ApplicationRepository<Subsidiary> {

    Subsidiary findByName(String name);

    Iterable<Subsidiary> findAllByIdNot(Long id);

}
