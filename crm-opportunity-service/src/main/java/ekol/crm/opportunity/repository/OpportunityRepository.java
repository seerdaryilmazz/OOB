package ekol.crm.opportunity.repository;

import ekol.crm.opportunity.domain.enumaration.OpportunityStatus;
import ekol.crm.opportunity.domain.model.Opportunity;
import ekol.hibernate5.domain.repository.ApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by Dogukan Sahinturk on 19.11.2019
 */
public interface OpportunityRepository extends ApplicationRepository<Opportunity> {
    Optional<Opportunity> findById(Long id);
    Page<Opportunity> findByAccountIdAndStatusIsNot(Pageable pageable, Long accountId, OpportunityStatus status);
    List<Opportunity> findByAccountId(Long accountId);

    @Query("select o from Opportunity o where (:accountId is null or o.account.id = :accountId) and (:status is null or o.status != :status)")
    Page<Opportunity> findAllByAccountId(
            Pageable pageable,
            @Param("accountId") Long accountId,
            @Param("status") OpportunityStatus status);

    List<Opportunity> findByExpectedQuoteDateBetweenAndStatusIn(LocalDate today,LocalDate date, Collection<OpportunityStatus> status);
}
