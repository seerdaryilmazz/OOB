package ekol.crm.account.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import ekol.crm.account.domain.enumaration.CustomsType;
import ekol.crm.account.domain.model.potential.Potential;
import ekol.hibernate5.domain.repository.ApplicationRepository;

public interface PotentialRepository extends ApplicationRepository<Potential>, JpaSpecificationExecutor<Potential> {

    Optional<Potential> findById(Long id);
    List<Potential> findByAccountId(Long accountId);
    Page<Potential> findByAccountIdAndServiceArea(Long accountId, String serviceArea, Pageable pageable);
    List<Potential> findByAccountIdAndCustomsType(Long accountId, CustomsType customsType);
}
