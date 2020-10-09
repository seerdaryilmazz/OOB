package ekol.kartoteks.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ekol.kartoteks.service.GooglePlacesService;

/**
 * Created by kilimci on 17/01/2017.
 */
@RestController
@RequestMapping("/google-places")
public class GooglePlacesController {

    @Autowired
    private GooglePlacesService googlePlacesService;

    @RequestMapping(value="/search",method= RequestMethod.GET)
    public Map search(@RequestParam String query){
        return googlePlacesService.search(query);
    }

    @RequestMapping(value="/details",method= RequestMethod.GET)
    public Map details(@RequestParam String placeId){
        return googlePlacesService.placeDetails(placeId);
    }

    @RequestMapping(value="/auto-complete",method= RequestMethod.GET)
    public Map autoComplete(@RequestParam String query, @RequestParam(required = false) String region){
        return googlePlacesService.autoComplete(query,region);
    }

    @RequestMapping(value="/reverse-geocode",method= RequestMethod.GET)
    public Map reverseGeocode(@RequestParam BigDecimal lat, @RequestParam  BigDecimal lng){
        return googlePlacesService.reverseGeocode(lat, lng);
    }
    @RequestMapping(value="/reverse-geocode-place",method= RequestMethod.GET)
    public Map reverseGeocodePlace(@RequestParam String placeId){
        return googlePlacesService.reverseGeocodePlace(placeId);
    }
    @RequestMapping(value="/timezone",method= RequestMethod.GET)
    public Map timezone(@RequestParam BigDecimal lat, @RequestParam  BigDecimal lng){
    	return googlePlacesService.timezone(lat, lng);
    }

}
