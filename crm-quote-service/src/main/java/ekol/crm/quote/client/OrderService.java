package ekol.crm.quote.client;

import org.springframework.beans.factory.annotation.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ekol.crm.quote.domain.dto.orderservice.Incoterm;
import ekol.crm.quote.util.Utils;

@Service
public class OrderService {

	@Value("${oneorder.order-service}")
    private String orderServiceName;

	@Autowired
    private RestTemplate restTemplate;

	@Cacheable("one-day-cache")
    public Incoterm findIncoterm(String code, boolean ignoreNotFoundException) {
        return Utils.getForObject(ignoreNotFoundException, restTemplate, orderServiceName + "/incoterm/byCode?code={code}", Incoterm.class, code);
    }
}
