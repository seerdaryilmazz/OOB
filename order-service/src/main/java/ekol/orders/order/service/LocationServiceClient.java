package ekol.orders.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

import ekol.model.IdNamePair;
import ekol.orders.order.domain.dto.response.location.CountryResponse;
import ekol.orders.order.domain.dto.response.location.CustomsOfficeLocationResponse;
import ekol.orders.order.domain.dto.response.location.CustomsOfficeResponse;
import ekol.orders.order.domain.dto.response.location.RegionResponse;
import ekol.orders.order.domain.dto.response.location.WarehouseResponse;

/**
 * Created by kilimci on 18/08/2017.
 */
@Component
public class LocationServiceClient {

    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    @Value("${oneorder.location-service}")
    private String locationService;

    public CustomsOfficeLocationResponse getCustomsOfficeLocation(Long customsOfficeLocationId) {
    	return oAuth2RestTemplate.getForObject(locationService + "/location/customs-office/location/" + customsOfficeLocationId, CustomsOfficeLocationResponse.class);
    }
    
    public WarehouseResponse findWarehouseById(Long id){
        return oAuth2RestTemplate.getForObject(locationService + "/location/warehouse/" + id, WarehouseResponse.class);
    }

    public WarehouseResponse findWarehouseByLocationId(Long id){
        return oAuth2RestTemplate.getForObject(locationService + "/location/warehouse/bycompanylocation/" + id, WarehouseResponse.class);
    }

    public RegionResponse getCollectionRegionOfCompanyLocation(Long locationId){
        return oAuth2RestTemplate.getForObject(locationService + "/collection-region/query-two/by-company-location?companyLocationId=" + locationId, RegionResponse.class);
    }

    public RegionResponse getDistributionRegionOfCompanyLocation(Long locationId){
        return oAuth2RestTemplate.getForObject(locationService + "/distribution-region/query-two/by-company-location?companyLocationId=" + locationId, RegionResponse.class);
    }
    
    public RegionResponse getCollectionRegionOfCustomsLocation(Long locationId){
    	return oAuth2RestTemplate.getForObject(locationService + "/collection-region/query-two/by-customs-location?customsLocationId=" + locationId, RegionResponse.class);
    }
    
    public RegionResponse getDistributionRegionOfCustomsLocation(Long locationId){
    	return oAuth2RestTemplate.getForObject(locationService + "/distribution-region/query-two/by-customs-location?customsLocationId=" + locationId, RegionResponse.class);
    }

    public boolean isCrossDockWarehouse(Long locationId){
        IdNamePair response =  oAuth2RestTemplate.getForObject(locationService + "/location/warehouse/bycompanylocation/" + locationId,
                IdNamePair.class);
        return response != null;
    }
    
    public CustomsOfficeResponse getCustomsOffice(Long customsOfficeId) {
    	return oAuth2RestTemplate.getForObject(locationService + "/location/customs-office/" + customsOfficeId, CustomsOfficeResponse.class);
    }

    public boolean isCustomsOfficeExists(Long customsOfficeId){
        return oAuth2RestTemplate.getForEntity(locationService + "/location/customs-office/" + customsOfficeId, IdNamePair.class)
                .getStatusCode().is2xxSuccessful();

    }
    public boolean isCustomsOfficeLocationExists(Long customsOfficeLocationId){
    	return oAuth2RestTemplate.getForEntity(locationService + "/location/customs-office/location/" + customsOfficeLocationId, IdNamePair.class)
    			.getStatusCode().is2xxSuccessful();
    	
    }

    public boolean isCustomsLocationExists(Long locationId){
        return oAuth2RestTemplate.getForEntity(locationService + "/location/customs/" + locationId, IdNamePair.class)
                .getStatusCode().is2xxSuccessful();
    }

    public CountryResponse getCountryDetails(String code){
        return oAuth2RestTemplate.getForObject(locationService + "/country/by-iso/" + code, CountryResponse.class);
    }
}
