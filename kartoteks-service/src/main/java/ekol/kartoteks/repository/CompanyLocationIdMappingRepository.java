package ekol.kartoteks.repository;


import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.kartoteks.domain.CompanyLocation;
import ekol.kartoteks.domain.CompanyLocationIdMapping;
import ekol.kartoteks.domain.RemoteApplication;

import java.util.List;

/**
 * Created by kilimci on 04/05/16.
 */
public interface CompanyLocationIdMappingRepository extends ApplicationRepository<CompanyLocationIdMapping> {

    List<CompanyLocationIdMapping> findByCompanyLocation(CompanyLocation companyLocation);
    List<CompanyLocationIdMapping> findByApplicationLocationId(String applicationLocationId);
    List<CompanyLocationIdMapping> findByCompanyLocationIdAndApplication(Long companyLocationId, RemoteApplication application);
    List<CompanyLocationIdMapping> findByCompanyLocationAndApplication(CompanyLocation companyLocation, RemoteApplication application);
    CompanyLocationIdMapping findByCompanyLocationAndApplicationAndApplicationLocationId(
            CompanyLocation companyLocation, RemoteApplication application, String applicationLocationId);

    CompanyLocationIdMapping findByApplicationAndApplicationLocationId(
            RemoteApplication application, String applicationLocationId);
}
