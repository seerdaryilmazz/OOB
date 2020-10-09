package ekol.location.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.location.domain.location.customs.CustomsOffice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CustomsOfficeRepository extends ApplicationRepository<CustomsOffice>, JpaSpecificationExecutor<CustomsOffice>{

    Optional<CustomsOffice> findById(Long id);

    @EntityGraph(value = "CustomsOffice.withLocationsAndContacts", type = EntityGraph.EntityGraphType.LOAD)
    Optional<CustomsOffice> findWithLocationsAndContactsById(Long id);

    List<CustomsOffice> findByBorderCustoms(boolean isBorder);
}
