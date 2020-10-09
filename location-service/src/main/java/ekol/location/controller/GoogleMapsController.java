package ekol.location.controller;

import ekol.location.service.GoogleMapsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by kilimci on 17/01/2017.
 */
@RestController
@RequestMapping("/google-maps")
public class GoogleMapsController {

    @Autowired
    private GoogleMapsService googleMapsService;

    @RequestMapping(value="/search",method= RequestMethod.GET)
    public Map search(@RequestParam String query){
        return googleMapsService.search(query);
    }

    @RequestMapping(value="/details",method= RequestMethod.GET)
    public Map details(@RequestParam String placeId){
        return googleMapsService.placeDetails(placeId);
    }

    @RequestMapping(value="/auto-complete",method= RequestMethod.GET)
    public Map autoComplete(@RequestParam String query){
        return googleMapsService.autoComplete(query);
    }

    @RequestMapping(value="/reverse-geocode",method= RequestMethod.GET)
    public Map reverseGeocode(@RequestParam BigDecimal lat, @RequestParam BigDecimal lng){
        return googleMapsService.reverseGeocode(lat, lng);
    }
    @RequestMapping(value="/reverse-geocode-place",method= RequestMethod.GET)
    public Map reverseGeocodePlace(@RequestParam String placeId){
        return googleMapsService.reverseGeocodePlace(placeId);
    }
    @RequestMapping(value="/timezone",method= RequestMethod.GET)
    public Map timezone(@RequestParam BigDecimal lat, @RequestParam BigDecimal lng){
        return googleMapsService.getTimezone(lat, lng);
    }

}
