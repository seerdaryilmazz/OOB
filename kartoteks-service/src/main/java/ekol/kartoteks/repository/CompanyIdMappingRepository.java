package ekol.kartoteks.repository;


import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.kartoteks.domain.Company;
import ekol.kartoteks.domain.CompanyIdMapping;
import ekol.kartoteks.domain.RemoteApplication;

import java.util.List;

/**
 * Created by kilimci on 03/05/16.
 */
public interface CompanyIdMappingRepository extends ApplicationRepository<CompanyIdMapping> {

    List<CompanyIdMapping> findByCompany(Company company);
    CompanyIdMapping findByCompanyAndApplicationAndApplicationCompanyId(Company company, RemoteApplication application, String applicationCompanyId);
    List<CompanyIdMapping> findByCompanyAndApplication(Company company, RemoteApplication application);
    CompanyIdMapping findByApplicationAndApplicationCompanyId(RemoteApplication application, String applicationCompanyId);
    List<CompanyIdMapping> findByApplicationCompanyId(String applicationCompanyId);
}
