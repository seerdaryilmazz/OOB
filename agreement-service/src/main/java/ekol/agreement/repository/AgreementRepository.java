package ekol.agreement.repository;

import ekol.agreement.domain.enumaration.AgreementStatus;
import ekol.agreement.domain.enumaration.StampTaxPayer;
import ekol.agreement.domain.model.agreement.Agreement;
import ekol.hibernate5.domain.repository.ApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AgreementRepository extends ApplicationRepository<Agreement> {
    Optional<Agreement> findById(Long id);

    @Query("select a from Agreement a where (:accountId is null or a.account.id = :accountId)")
    Page<Agreement> findAllByAccountId(
            Pageable pageable,
            @Param("accountId") Long accountId);

    List<Agreement> findAllByAccountId(Long accountId);

    Iterable<Agreement> findByEndDateBetweenAndStatusIsNot(LocalDate today, LocalDate endDate, AgreementStatus status);
    Iterable<Agreement> findByInsuranceInfos_validityEndDateBetweenAndStatusIsNot(LocalDate today, LocalDate validityEndDate, AgreementStatus status);
    Iterable<Agreement> findByLetterOfGuarentees_validityEndDateBetweenAndStatusIsNot(LocalDate today, LocalDate validityEndDate, AgreementStatus status);
    Iterable<Agreement> findByUnitPrices_validityEndDateBetweenAndStatusIsNot(LocalDate today, LocalDate validityEndDate, AgreementStatus status);
    Iterable<Agreement> findByAutoRenewalDateBetweenAndStatusIsNot(LocalDate today, LocalDate autoRenewalDate, AgreementStatus status);
    Iterable<Agreement> findByKpiInfos_nextUpdateDateBetweenAndStatusIsNot(LocalDate today, LocalDate nextUpdateDate, AgreementStatus status);

    @Query("select a from Agreement a where a.financialInfo.stampTaxDueDate between :today and :date and (a.financialInfo.paid is null or a.financialInfo.paid =:paid) and a.financialInfo.stampTaxPayer != :stampTaxPayer and a.status != :status")
    Iterable<Agreement> findByStampTaxDueDateForNotify(
            @Param("today") LocalDate today,
            @Param("date") LocalDate date,
            @Param("status") AgreementStatus status,
            @Param("stampTaxPayer") StampTaxPayer stampTaxPayer,
            @Param("paid") Boolean paid
    );
}
