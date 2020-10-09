package ekol.crm.quote.client;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import ekol.crm.quote.domain.dto.kartoteksservice.*;
import ekol.crm.quote.util.Utils;
import ekol.model.CodeNamePair;

@Service
public class KartoteksService {

	@Value("${oneorder.kartoteks-service}")
    private String kartoteksServiceName;

	@Autowired
    private RestTemplate restTemplate;

    public Company findCompanyById(Long id, boolean ignoreNotFoundException) {
        return Utils.getForObject(ignoreNotFoundException, restTemplate, kartoteksServiceName + "/company/{id}" , Company.class, id);
    }

    public CompanyLocation findLocationById(Long id, boolean ignoreNotFoundException) {
        return Utils.getForObject(ignoreNotFoundException, restTemplate, kartoteksServiceName + "/location/{id}", CompanyLocation.class, id);
    }

    public CompanyContact findContactById(Long id, boolean ignoreNotFoundException) {
        return Utils.getForObject(ignoreNotFoundException, restTemplate, kartoteksServiceName + "/contact/{id}", CompanyContact.class, id);
    }

    @Cacheable("one-week-cache")
    public CodeNamePair findServiceAreaByCode(String code, boolean ignoreNotFoundException){
        return Utils.getForObject(ignoreNotFoundException, restTemplate, kartoteksServiceName + "/business-segment-type/code/{code}", CodeNamePair.class, code);
    }

    public boolean isTaxIdVerified(Long companyId) {
        boolean verified = false;
        try {
        	restTemplate.getForEntity(kartoteksServiceName + "/company/{companyId}/verify-tax-id?bestEffortVerification=true", String.class, companyId);
            verified = true;
        } catch (HttpClientErrorException e) {
            if (HttpStatus.BAD_REQUEST != e.getStatusCode()) {
            	throw e;
            }
        }
        return verified;
    }

    public boolean isEoriNumberVerified(Long companyId) {
        boolean verified = false;
        try {
        	restTemplate.getForEntity(kartoteksServiceName + "/company/{companyId}/verify-eori-number", String.class, companyId);
            verified = true;
        } catch (HttpClientErrorException e) {
        	if (HttpStatus.BAD_REQUEST != e.getStatusCode()) {
        		throw e;
        	}
        }
        return verified;
    }

    public CompanyImportQueue findLastImportQueueItemForLocation(String application, Long companyId, Long locationId, boolean ignoreNotFoundException) {
        List<String> params = Arrays.asList("application={application}", "companyId={companyId}", "locationId={locationId}");
        return Utils.getForObject(ignoreNotFoundException, restTemplate, kartoteksServiceName + "/import-queue/last-import-queue-item-for-location?" + StringUtils.join(params, "&"), CompanyImportQueue.class, application, companyId, locationId);
    }

    public boolean isContactLinkedToLocation(Long contactId, Long locationId) {
        return restTemplate.getForObject(kartoteksServiceName + "/contact/{contactId}/is-linked-to-location?locationId={locationId}", Boolean.class, contactId, locationId).booleanValue();
    }

    public boolean isContactActive(Long contactId) {
        return restTemplate.getForObject(kartoteksServiceName + "/contact/{contactId}/is-active", Boolean.class, contactId).booleanValue();
    }

    public boolean isLocationActive(Long locationId) {
        return restTemplate.getForObject(kartoteksServiceName + "/location/{locationId}/is-active", Boolean.class, locationId).booleanValue();
    }

    public BulkExistenceAndActivenessCheckResponse doBulkExistenceAndActivenessCheckForContacts(List<Long> ids) {
        return restTemplate.getForObject("/contact/bulk-existence-and-activeness-check?commaSeparatedIds={ids}", BulkExistenceAndActivenessCheckResponse.class, StringUtils.join(ids, ","));
    }

    public BulkExistenceAndActivenessCheckResponse doBulkExistenceAndActivenessCheckForLocations(List<Long> ids) {
        return restTemplate.getForObject(kartoteksServiceName + "/location/bulk-existence-and-activeness-check?commaSeparatedIds={ids}", BulkExistenceAndActivenessCheckResponse.class, StringUtils.join(ids, ","));
    }
}
