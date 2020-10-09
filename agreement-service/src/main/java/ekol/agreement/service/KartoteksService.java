package ekol.agreement.service;

import ekol.agreement.domain.dto.kartoteks.Company;
import ekol.agreement.domain.dto.kartoteks.CompanyImportQueue;
import ekol.agreement.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Dogukan Sahinturk on 11.10.2019
 */
@Service
public class KartoteksService {

	@Value("${oneorder.kartoteks-service}")
    private String kartoteksServiceName;

	@Autowired
    private RestTemplate restTemplate;

    public Company findCompanyById(Long id, boolean ignoreNotFoundException) {
        return Utils.getForObject(ignoreNotFoundException, restTemplate, kartoteksServiceName + "/company/{id}" , Company.class, id);
    }

    public CompanyImportQueue findLastImportQueueItemForLocation(String application, Long companyId, Long locationId, boolean ignoreNotFoundException) {
        List<String> params = Arrays.asList("application={application}", "companyId={companyId}", "locationId={locationId}");
        return Utils.getForObject(ignoreNotFoundException, restTemplate, kartoteksServiceName + "/import-queue/last-import-queue-item-for-location?" + StringUtils.join(params, "&"), CompanyImportQueue.class, application, companyId, locationId);
    }
}
