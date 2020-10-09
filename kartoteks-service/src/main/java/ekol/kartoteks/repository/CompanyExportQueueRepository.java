package ekol.kartoteks.repository;


import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.kartoteks.domain.CompanyExportQueue;
import ekol.kartoteks.domain.ExportQueueStatus;

import java.util.List;

/**
 * Created by kilimci on 01/08/16.
 */
public interface CompanyExportQueueRepository extends ApplicationRepository<CompanyExportQueue> {

    List<CompanyExportQueue> findByStatusOrderByCreateDateAsc(ExportQueueStatus status);
}
