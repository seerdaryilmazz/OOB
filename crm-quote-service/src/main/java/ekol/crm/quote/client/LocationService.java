package ekol.crm.quote.client;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ekol.crm.quote.domain.dto.locationservice.WarehouseJson;

@Component
public class LocationService {
	@Value("${oneorder.location-service}")
    private String locationServiceName;

	@Autowired
    private RestTemplate restTemplate;

	public WarehouseJson getWarehouseByCompanyLocationId(Long locationId) {
		return restTemplate.getForObject(locationServiceName + "/location/warehouse/bycompanylocation/{locationId}", WarehouseJson.class, locationId);
	}
}
