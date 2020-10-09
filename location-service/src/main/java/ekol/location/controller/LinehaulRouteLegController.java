package ekol.location.controller;

import ekol.exceptions.BadRequestException;
import ekol.location.domain.LinehaulRouteLeg;
import ekol.location.domain.RouteLegType;
import ekol.location.domain.dto.RouteResponse;
import ekol.location.service.LinehaulRouteLegService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/linehaul-route-leg")
public class LinehaulRouteLegController {

    @Autowired
    private LinehaulRouteLegService linehaulRouteLegService;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public List<LinehaulRouteLeg> findAll(@RequestParam(required = false) String type, @RequestParam(required = false) Long fromId) {
        if (StringUtils.isNotBlank(type)) {
            return linehaulRouteLegService.findAllWithDetailsByType(RouteLegType.valueOf(type));
        }
        if (fromId != null) {
            return linehaulRouteLegService.findAllWithDetailsByFromId(fromId);
        }
        return linehaulRouteLegService.findAllWithDetails();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public LinehaulRouteLeg find(@PathVariable Long id) {
        return linehaulRouteLegService.findWithDetailsByIdOrThrowResourceNotFoundException(id);
    }

    @RequestMapping(value = {"/between-leg-stops"}, method = RequestMethod.GET)
    public Iterable<LinehaulRouteLeg> findAllBetweenLegStops(@RequestParam(required = false) Long fromId, @RequestParam(required = false) Long toId) {
        return linehaulRouteLegService.findAllBetweenLegStops(fromId, toId);
    }

    @RequestMapping(value = {"/between-leg-stops/as-locations/with-available-expeditions"}, method = RequestMethod.GET)
    public Iterable<RouteResponse.RouteLegResponse> findAllBetweenLegStopsAsLocations(@RequestParam(required = false) Long fromId, @RequestParam(required = false) Long toId) {
        return linehaulRouteLegService.findAllBetweenLegStopsAsLocations(fromId, toId);
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public LinehaulRouteLeg create(@RequestBody LinehaulRouteLeg routeLeg) {

        if (routeLeg != null && routeLeg.getId() != null) {
            throw new BadRequestException("This method must be used for creation.");
        }

        return linehaulRouteLegService.createOrUpdate(routeLeg);
    }

    @RequestMapping(value = {"/{id}", "/{id}/"}, method = RequestMethod.PUT)
    public LinehaulRouteLeg update(@PathVariable Long id, @RequestBody LinehaulRouteLeg routeLeg) {

        if (!id.equals(routeLeg.getId())) {
            throw new BadRequestException("LinehaulRoute.id must be " + id + ".");
        }

        return linehaulRouteLegService.createOrUpdate(routeLeg);
    }

    @RequestMapping(value = {"/{id}", "/{id}/"}, method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        linehaulRouteLegService.softDelete(id);
    }
}
