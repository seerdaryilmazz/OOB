package ekol.kartoteks.domain.export;

import ekol.kartoteks.domain.Company;
import ekol.kartoteks.domain.CompanyContactIdMapping;
import ekol.kartoteks.domain.CompanyLocationIdMapping;
import ekol.kartoteks.domain.RemoteApplication;
import ekol.kartoteks.domain.exchange.CompanyExchangeData;
import ekol.kartoteks.repository.CompanyContactIdMappingRepository;
import ekol.kartoteks.repository.CompanyLocationIdMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by kilimci on 06/06/16.
 */
@Component
public class MultiLocationExportDataPreparationStrategy extends AbstractCompanyExportDataPreparation {

    @Autowired
    private CompanyLocationIdMappingRepository companyLocationIdMappingRepository;
    @Autowired
    private CompanyContactIdMappingRepository companyContactIdMappingRepository;

    @Override
    public CompanyExchangeData prepareExportData(RemoteApplication application, String applicationCompanyId, Company company) {
        CompanyExchangeData companyExchangeData = CompanyExchangeData.fromCompany(company);
        companyExchangeData.setCompanyId(applicationCompanyId);
        companyExchangeData.getLocations().forEach(locationExchangeData -> {
            List<CompanyLocationIdMapping> locationMappings = companyLocationIdMappingRepository.findByCompanyLocationIdAndApplication(
                    locationExchangeData.getKartoteksId(), application);
            if(!locationMappings.isEmpty()){
                locationExchangeData.setLocationId(locationMappings.get(0).getApplicationLocationId());
            }
        });
        companyExchangeData.getContacts().forEach(contactExchangeData -> {
            List<CompanyContactIdMapping> contactMappings = companyContactIdMappingRepository.findByCompanyContactIdAndApplication(
                    contactExchangeData.getKartoteksId(), application);
            if(!contactMappings.isEmpty()){
                contactExchangeData.setContactId(contactMappings.get(0).getApplicationContactId());
            }
        });
        return companyExchangeData;
    }
}
