package ekol.kartoteks.domain.export;


import ekol.kartoteks.domain.Company;
import ekol.kartoteks.domain.CompanyLocation;
import ekol.kartoteks.domain.RemoteApplication;
import ekol.kartoteks.domain.exchange.CompanyExchangeData;

/**
 * Created by kilimci on 06/06/16.
 */
public interface CompanyExportDataPreparationStrategy {
    CompanyExchangeData prepareExportData(RemoteApplication application, String applicationCompanyId, Company company);
    CompanyExchangeData prepareExportData(RemoteApplication application, String applicationCompanyId,
                                          String applicationLocationCompanyId, CompanyLocation companyLocation);
}
