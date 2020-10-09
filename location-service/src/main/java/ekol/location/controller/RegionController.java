package ekol.location.controller;

import ekol.location.domain.Region;
import ekol.location.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ozer on 13/12/16.
 */
@RestController
public class RegionController {

    @Autowired
    private RegionService regionService;

    @RequestMapping(value = "/region", method = RequestMethod.GET)
    public Iterable<Region> findAll() {
        return regionService.findAll();
    }
}
