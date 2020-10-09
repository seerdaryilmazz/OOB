package ekol.crm.search.client;

import ekol.crm.search.event.dto.agreement.AgreementJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AgreementServiceClient {

    @Value("${oneorder.agreement-service}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    public AgreementJson getAgreement(Long agreementId) {
        return restTemplate.getForObject(url + "/agreement/{agreementId}", AgreementJson.class, agreementId);
    }
}
