package ekol.crm.quote.queue.common.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

import ekol.crm.quote.queue.common.dto.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor=@__(@Autowired))
public class LocationServiceClient {
	@Value("${oneorder.location-service}")
    private String locationService;

	@NonNull
    private OAuth2RestTemplate restTemplate;
	
	public CustomsOfficeJson getCustomsOffice(Long customerOfficeId) {
		return restTemplate.getForObject(locationService + "/location/customs-office/{customerOfficeId}", CustomsOfficeJson.class, customerOfficeId);
	}
	public WarehouseJson getCustomsByCompanyLocation(Long locationId) {
		return restTemplate.getForObject(locationService + "/location/customs/by-location/{locationId}", WarehouseJson.class, locationId);
	}
	public PlaceJson getPlaceByCompanyLocation(Long locationId) {
		return restTemplate.getForObject(locationService + "/location/customs/place/{locationId}", PlaceJson.class, locationId);
	}
}
