package ekol.location.controller;

import ekol.location.domain.City;
import ekol.location.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ozer on 13/12/16.
 */
@RestController
public class CityController {

    @Autowired
    private CityService cityService;

    @RequestMapping(value = "/city", method = RequestMethod.GET)
    public Iterable<City> findAll() {
        return cityService.findAll();
    }
}
