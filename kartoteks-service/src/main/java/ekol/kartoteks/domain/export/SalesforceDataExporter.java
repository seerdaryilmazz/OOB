package ekol.kartoteks.domain.export;


import ekol.kartoteks.domain.CompanyExportQueue;
import ekol.kartoteks.domain.exchange.QueueExchangeData;
import ekol.kartoteks.domain.export.connection.ExternalSystemConnectionProperties;
import ekol.kartoteks.service.ExternalSystemUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by kilimci on 12/07/16.
 */
@Component
public class SalesforceDataExporter implements ExternalSystemDataExporter {

    @Autowired
    @Qualifier(value = "salesforceConnectionProperties")
    private ExternalSystemConnectionProperties connectionProperties;

    @Autowired
    private ExternalSystemUpdateService externalSystemUpdateService;

    @Override
    public void exportCompany(CompanyExportQueue queue) {
        externalSystemUpdateService.postToExternalServer(QueueExchangeData.fromJson(queue.getData()), connectionProperties);
    }
}
