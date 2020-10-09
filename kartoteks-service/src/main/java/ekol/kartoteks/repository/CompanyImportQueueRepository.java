package ekol.kartoteks.repository;


import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.kartoteks.domain.CompanyImportQueue;
import ekol.kartoteks.domain.ImportQueueStatus;
import ekol.kartoteks.domain.RemoteApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by fatmaozyildirim on 4/8/16.
 */
public interface CompanyImportQueueRepository extends ApplicationRepository<CompanyImportQueue> {

    List<CompanyImportQueue> findByStatus(ImportQueueStatus status);
    CompanyImportQueue findFirstByStatus(ImportQueueStatus status);
    List<CompanyImportQueue> findByStatusOrCreateDateOrApplication(ImportQueueStatus status, Date createDate, String application);

    List<CompanyImportQueue> findByApplication(String application);

    List<CompanyImportQueue> findByCreateDate(Date createDate);

    List<CompanyImportQueue> findByCompanyNameAndStatus(String companyName, ImportQueueStatus status);

    List<CompanyImportQueue> findByCompanyIdAndStatus(Long companyId, ImportQueueStatus status);
    List<CompanyImportQueue> findByApplicationCompanyIdAndStatus(String applicationCompanyId, ImportQueueStatus status);

    @Query(value = "select ciq.status, count(ciq) from CompanyImportQueue ciq where ciq.createDate < :createDate group by ciq.status")
    List<Object[]> getStatsByLessThanCreateDate(@Param("createDate") Date createDate);

    @Query(value = "select ciq.status, count(ciq) from CompanyImportQueue ciq group by ciq.status")
    List<Object[]> getAllStats();

    @Query(value = "select ciq.status, count(ciq) from CompanyImportQueue ciq where ciq.createDate.dateTime > :createDate group by ciq.status")
    List<Object[]> getStatsByGreaterThanCreateDate(@Param("createDate") LocalDateTime createDate);

    @Query(value = "select ciq from CompanyImportQueue ciq where ciq.completedBy = :completedBy and ciq.status = :status and ciq.completedDate.dateTime > :completedDate")
    List<CompanyImportQueue> getByUserAndStatusAndCompletedDateGreaterThan(@Param("completedDate") LocalDateTime completedDate, @Param("completedBy") String username, @Param("status") ImportQueueStatus status);

    @Query(value = "select ciq from CompanyImportQueue ciq where ciq.status = :status and ciq.completedDate.dateTime > :completedDate and ciq.completedBy is not null")
    List<CompanyImportQueue> getByStatusAndCompletedDateGreaterThan(@Param("completedDate") LocalDateTime completedDate, @Param("status") ImportQueueStatus status);

    @Query(value = "select ciq from CompanyImportQueue ciq where ciq.status = 'PENDING' and ciq.createDate.dateTime > :createDate")
    List<CompanyImportQueue> getPendingItemsFromCreateDate(@Param("createDate") LocalDateTime createDate);

    @Query(value = "select ciq from CompanyImportQueue ciq where ciq.status = 'SUCCESS' and ciq.completedDate.dateTime > :completedDate")
    List<CompanyImportQueue> getCompletedItemsFromDate(@Param("completedDate") LocalDateTime completedDate);

    Page<CompanyImportQueue> findByApplicationAndCompanyId(RemoteApplication application, Long companyId, Pageable pageable);

}
