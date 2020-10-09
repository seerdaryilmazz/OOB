package ekol.crm.account.repository;

import ekol.crm.account.domain.model.CustomsOffice;
import ekol.crm.account.domain.model.potential.Potential;
import ekol.hibernate5.domain.repository.ApplicationRepository;

import java.util.List;
import java.util.Optional;

public interface CustomsOfficeRepository extends ApplicationRepository<CustomsOffice> {

    Optional<CustomsOffice> findById(Long id);
    List<CustomsOffice> findByPotential(Potential potential);
}
