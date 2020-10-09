package ekol.location.controller;

import ekol.location.domain.PolygonRegion;
import ekol.location.domain.Zone;
import ekol.location.service.PolygonRegionService;
import ekol.location.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ozer on 13/12/16.
 */
@RestController
@RequestMapping("/zone")
public class ZoneController {

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private PolygonRegionService polygonRegionService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Zone findWithDetailsById(@PathVariable Long id) {
        return zoneService.findWithDetailsById(id);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Zone> findAll() {
        return zoneService.findAll();
    }

    @RequestMapping(value = "/zone-type/{zoneType}", method = RequestMethod.GET)
    public Iterable<Zone> findAll(@PathVariable String zoneType) {
        return zoneService.findAll(zoneType);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void save(@RequestBody Zone zone) {
        zoneService.save(zone);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        zoneService.delete(id);
    }

    // TODO: Bu metod şu an için dışarıdan çağırılmıyor, koordinatlardan PolygonRegion bulmayı test edebilmek için oluşturuldu.
    @RequestMapping(value = "/by-lat-lng", method = RequestMethod.GET)
    public List<PolygonRegion> findAllThatContainPoint(@RequestParam("lat") BigDecimal lat, @RequestParam("lng") BigDecimal lng) {

        long start = System.currentTimeMillis();

        List<PolygonRegion> list = polygonRegionService.findAnyLevelPolygonRegionThatContainsPoint(lat, lng);

        long end = System.currentTimeMillis();

        System.out.println();
        System.out.println((end - start) + " milliseconds");
        System.out.println();

        return list;
    }

    @RequestMapping(value = "/distribution/bylocationid/{locationId}", method = RequestMethod.GET)
    public Zone findReceiverZoneByLocationId(@PathVariable Long locationId) {
        return zoneService.findZoneByLocationId(locationId, ZoneService.ZONETYPE_DISTRIBUTION_CODE);
    }

    @RequestMapping(value = "/collection/bylocationid/{locationId}", method = RequestMethod.GET)
    public Zone findSenderZoneByLocationId(@PathVariable Long locationId) {
        return zoneService.findZoneByLocationId(locationId, ZoneService.ZONETYPE_COLLECTION_CODE);
    }
}
