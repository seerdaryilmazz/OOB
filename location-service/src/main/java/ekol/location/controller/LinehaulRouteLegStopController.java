package ekol.location.controller;

import ekol.exceptions.BadRequestException;
import ekol.location.domain.LinehaulRouteLegStop;
import ekol.location.domain.LocationType;
import ekol.location.domain.RouteLegType;
import ekol.location.service.LinehaulRouteLegStopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/linehaul-route-leg-stop")
public class LinehaulRouteLegStopController {

    @Autowired
    private LinehaulRouteLegStopService linehaulRouteLegStopService;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public List<LinehaulRouteLegStop> findAll() {
        return linehaulRouteLegStopService.findAllWithDetails();
    }

    @RequestMapping(value = "/by-route-leg-type/{routeLegType}", method = RequestMethod.GET)
    public Iterable<LinehaulRouteLegStop> find(@PathVariable RouteLegType routeLegType) {
        return linehaulRouteLegStopService.findByRouteLegTypeWithDetails(routeLegType);
    }

    @RequestMapping(value = "/by-location-type/{locationType}", method = RequestMethod.GET)
    public Iterable<LinehaulRouteLegStop> find(@PathVariable LocationType locationType) {
        return linehaulRouteLegStopService.findByLocationTypeWithDetails(locationType);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public LinehaulRouteLegStop find(@PathVariable Long id) {
        return linehaulRouteLegStopService.findWithDetailsByIdOrThrowResourceNotFoundException(id);
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public LinehaulRouteLegStop create(@RequestBody LinehaulRouteLegStop stop) {

        if (stop != null && stop.getId() != null) {
            throw new BadRequestException("This method must be used for creation.");
        }

        return linehaulRouteLegStopService.createOrUpdate(stop);
    }

    @RequestMapping(value = {"/{id}", "/{id}/"}, method = RequestMethod.PUT)
    public LinehaulRouteLegStop update(@PathVariable Long id, @RequestBody LinehaulRouteLegStop stop) {

        if (!id.equals(stop.getId())) {
            throw new BadRequestException("LinehaulRoute.id must be " + id + ".");
        }

        return linehaulRouteLegStopService.createOrUpdate(stop);
    }

    @RequestMapping(value = {"/{id}", "/{id}/"}, method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        linehaulRouteLegStopService.softDelete(id);
    }
}
