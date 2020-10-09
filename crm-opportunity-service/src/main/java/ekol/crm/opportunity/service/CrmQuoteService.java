package ekol.crm.opportunity.service;

import ekol.crm.opportunity.domain.dto.QuoteJson;
import ekol.crm.opportunity.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by Dogukan Sahinturk on 8.01.2020
 */
@Component
public class CrmQuoteService {

    @Value("${oneorder.crm-quote-service}")
    private String quoteServiceName;

    @Autowired
    private RestTemplate restTemplate;

    public List<QuoteJson> getQuotesCreatedFromOpportunity(Long id, boolean ignoreNotFoundException){
        String url = quoteServiceName + "/quote/quotes-by-attribute?attributeKey=opportunity&attributeValue={id}&ignoreCanceled=false";
        return Optional.ofNullable(Utils.getForObject(ignoreNotFoundException, restTemplate, url, QuoteJson[].class, String.valueOf(id))).map(Arrays::asList).orElseGet(ArrayList::new);
    }
}
