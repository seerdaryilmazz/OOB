package ekol.orders.order.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

import ekol.orders.order.domain.Order;
import ekol.orders.order.domain.OrderShipment;

@Component
public class ProjectServiceClient {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    @Value("${oneorder.project-service}")
    private String projectService;

    public LocalDateTime calculateDeliveryDate(Order order, OrderShipment shipment){

        Map<String, Object> request = new HashMap<>();
        request.put("customerId", order.getCustomer().getId());
        request.put("loadType", order.getTruckLoadType().name());
        request.put("serviceType", order.getServiceType().name());
        request.put("readyDate", formatter.format(shipment.findReadyDate().getDateTime()));
        request.put("loadingLocationId", shipment.getSender().getHandlingLocation().getId());
        request.put("unloadingLocationId", shipment.getConsignee().getHandlingLocation().getId());
        request.put("loadingCompanyType", shipment.getSender().getHandlingCompanyType().name());
        request.put("unloadingCompanyType", shipment.getConsignee().getHandlingCompanyType().name());

        Map response = oAuth2RestTemplate.postForObject(projectService + "/calculate-delivery-date", request, Map.class);
        String deliveryDate = (String)response.get("deliveryDate");
        if(StringUtils.isBlank(deliveryDate)){
            return null;
        }
        return LocalDateTime.parse(deliveryDate, formatter);
    }
}
