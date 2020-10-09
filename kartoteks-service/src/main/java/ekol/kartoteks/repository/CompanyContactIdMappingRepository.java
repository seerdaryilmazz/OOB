package ekol.kartoteks.repository;

import ekol.hibernate5.domain.repository.ApplicationRepository;
import ekol.kartoteks.domain.CompanyContact;
import ekol.kartoteks.domain.CompanyContactIdMapping;
import ekol.kartoteks.domain.RemoteApplication;

import java.util.List;

/**
 * Created by kilimci on 09/02/2017.
 */
public interface CompanyContactIdMappingRepository extends ApplicationRepository<CompanyContactIdMapping> {

    List<CompanyContactIdMapping> findByCompanyContact(CompanyContact companyContact);
    List<CompanyContactIdMapping> findByApplicationContactId(String applicationContactId);
    List<CompanyContactIdMapping> findByCompanyContactIdAndApplication(Long companyContactId, RemoteApplication application);
    List<CompanyContactIdMapping> findByCompanyContactAndApplication(CompanyContact companyContact, RemoteApplication application);
    CompanyContactIdMapping findByCompanyContactAndApplicationAndApplicationContactId(
            CompanyContact companyContact, RemoteApplication application, String applicationContactId);

    CompanyContactIdMapping findByApplicationAndApplicationContactId(
            RemoteApplication application, String applicationContactId);
}
