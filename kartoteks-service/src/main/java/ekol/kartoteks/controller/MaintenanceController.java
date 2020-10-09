package ekol.kartoteks.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.kartoteks.domain.CompanyLocation;
import ekol.kartoteks.domain.Point;
import ekol.kartoteks.repository.CompanyLocationRepository;
import ekol.kartoteks.service.GooglePlacesService;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {

	@Autowired
	private CompanyLocationRepository companyLocationRepository;
	
	@Autowired
	private GooglePlacesService googlePlacesService;
	
	@RequestMapping("/fix-locations-empty-timezone")
	public void fixLocationsEmptyTimezone() {
		List<CompanyLocation> locations = companyLocationRepository.findByTimezoneIsNull();
		
		for (Iterator<CompanyLocation> it = locations.iterator(); it.hasNext();) {
			CompanyLocation location = it.next();
			Point point = location.getPostaladdress().getPointOnMap();
			if(Objects.nonNull(point) && Objects.nonNull(point.getLat()) && Objects.nonNull(point.getLng())) {
				Map response = googlePlacesService.timezone(point.getLat(), point.getLng());
				if("OK".equals(response.get("status"))) {
					location.setTimezone(response.containsKey("timeZoneId")?String.valueOf(response.get("timeZoneId")):null);
				}
			}
		}
		companyLocationRepository.save(locations);
	}
}
