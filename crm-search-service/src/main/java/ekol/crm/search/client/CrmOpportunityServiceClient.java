package ekol.crm.search.client;

import ekol.crm.search.event.dto.opportunity.OpportunityJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Dogukan Sahinturk on 15.01.2020
 */
@Component
public class CrmOpportunityServiceClient {
    @Value("${oneorder.crm-opportunity-service}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    public OpportunityJson getOpportunity(Long opportunityId) {
        return restTemplate.getForObject(url + "/opportunity/{opportunityId}", OpportunityJson.class, opportunityId);
    }
}
