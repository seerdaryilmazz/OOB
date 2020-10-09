package ekol.kartoteks.domain.export;

import ekol.kartoteks.domain.CompanyLocation;
import ekol.kartoteks.domain.RemoteApplication;
import ekol.kartoteks.domain.exchange.CompanyExchangeData;
import ekol.kartoteks.domain.exchange.LocationExchangeData;

import java.util.Arrays;

/**
 * Created by kilimci on 13/07/16.
 */
public abstract class AbstractCompanyExportDataPreparation implements CompanyExportDataPreparationStrategy{

    public CompanyExchangeData prepareExportData(RemoteApplication application, String applicationCompanyId, String applicationLocationId, CompanyLocation companyLocation){
        CompanyExchangeData companyExchangeData = CompanyExchangeData.fromCompany(companyLocation.getCompany());
        companyExchangeData.setCompanyId(applicationCompanyId);
        LocationExchangeData locationExchangeData = LocationExchangeData.fromLocation(companyLocation);
        locationExchangeData.setLocationId(applicationLocationId);
        companyExchangeData.setLocations(Arrays.asList(locationExchangeData));
        return companyExchangeData;
    }
}
