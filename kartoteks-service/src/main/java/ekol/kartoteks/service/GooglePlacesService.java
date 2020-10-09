package ekol.kartoteks.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kilimci on 17/01/2017.
 */
@Service
public class GooglePlacesService {

    @Value("${google.apikey}")
    private String key;
    
    @Value("${google.maps.api-url:https://maps.googleapis.com}")
    private String googleMapsApiUrl;

    private RestTemplate restTemplate = new RestTemplate();

    public Map search(String query){
        return restTemplate.getForObject(googleMapsApiUrl + "/maps/api/place/textsearch/json?query={query}&key={key}", HashMap.class, query, key);
    }
    public Map placeDetails(String placeId){
        return restTemplate.getForObject(googleMapsApiUrl + "/maps/api/place/details/json?placeid={placeId}&key={key}&language=en", HashMap.class, placeId, key);
    }
    public Map autoComplete(String query, String region){
        return restTemplate.getForObject(googleMapsApiUrl + "/maps/api/place/autocomplete/json?input={query}&region={region}&key={key}&language=en", HashMap.class, query, region, key);
    }
    public Map reverseGeocode(BigDecimal lat, BigDecimal lng){
        return restTemplate.getForObject(googleMapsApiUrl + "/maps/api/geocode/json?latlng={latlng}&key={key}&language=en", HashMap.class, lat.toString() + "," + lng.toString(), key);
    }
    public Map reverseGeocodePlace(String placeId){
        return restTemplate.getForObject(googleMapsApiUrl + "/maps/api/geocode/json?place_id={placeId}&key={key}&language=en", HashMap.class, placeId, key);
    }
    public Map timezone(BigDecimal lat, BigDecimal lng){
    	String latlng = lat.toString() + "," + lng.toString();
    	String timestamp = String.valueOf(Timestamp.valueOf(LocalDateTime.now()).getTime()/1000);
    	return restTemplate.getForObject(googleMapsApiUrl + "/maps/api/timezone/json?location={latlng}&timestamp={timestamp}&key={key}&language=en", HashMap.class, latlng, timestamp, key);
    }
}
