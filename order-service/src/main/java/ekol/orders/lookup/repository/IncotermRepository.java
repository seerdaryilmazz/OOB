package ekol.orders.lookup.repository;

import ekol.hibernate5.domain.repository.LookupRepository;
import ekol.orders.lookup.domain.Incoterm;

import java.util.Optional;

public interface IncotermRepository extends LookupRepository<Incoterm> {

    Optional<Incoterm> findById(Long id);
    Optional<Incoterm> findByCodeAndActiveTrue(String code);
    Optional<Incoterm> findByNameIgnoreCaseAndActiveTrue(String name);

}
