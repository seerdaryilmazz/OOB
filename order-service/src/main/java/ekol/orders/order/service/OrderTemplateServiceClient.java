package ekol.orders.order.service;

import ekol.orders.order.domain.dto.response.ShipmentLoadSpecRuleResponse;
import ekol.orders.order.domain.dto.response.UnitLoadSpecRuleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class OrderTemplateServiceClient {

    @Value("${oneorder.order-template-service}")
    private String templateService;

    @Autowired
    private OAuth2RestTemplate restTemplate;

    @Autowired
    public OrderTemplateServiceClient(OAuth2RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public ShipmentLoadSpecRuleResponse runLoadSpecRulesForShipment(BigDecimal grossWeight, BigDecimal ldm, BigDecimal valueOfGoodsAmount){
        Map<String, BigDecimal> params = new HashMap<>();
        params.put("grossWeight", grossWeight);
        params.put("ldm", ldm);
        params.put("valueOfGoodsAmount", valueOfGoodsAmount);

        return restTemplate.getForObject(
                templateService + "/rule/execute/load-spec-rule/for-shipment?grossWeight={grossWeight}&ldm={ldm}&value={valueOfGoodsAmount}",
                ShipmentLoadSpecRuleResponse.class, params);
    }

    public UnitLoadSpecRuleResponse runLoadSpecRulesForUnit(BigDecimal width, BigDecimal length, BigDecimal height){
        Map<String, BigDecimal> params = new HashMap<>();
        params.put("width", width);
        params.put("length", length);
        params.put("height", height);

        return restTemplate.getForObject(
                templateService + "/rule/execute/load-spec-rule/for-package?width={width}&length={length}&height={height}",
                UnitLoadSpecRuleResponse.class, params);
    }
}
