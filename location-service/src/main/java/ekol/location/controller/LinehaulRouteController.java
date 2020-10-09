package ekol.location.controller;

import ekol.exceptions.BadRequestException;
import ekol.location.domain.LinehaulRoute;
import ekol.location.domain.dto.RouteResponse;
import ekol.location.service.LinehaulRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/linehaul-route")
public class LinehaulRouteController {

    @Autowired
    private LinehaulRouteService linehaulRouteService;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public List<LinehaulRoute> findAll(@RequestParam(required = false) Long fromId, @RequestParam(required = false) Long toId) {
        return linehaulRouteService.findAllWithDetailsByFromId(fromId, toId);
    }

    @RequestMapping(value = {"/between-warehouses"}, method = RequestMethod.GET)
    public List<LinehaulRoute> findAllForWarehouses(@RequestParam(required = false) Long fromId, @RequestParam(required = false) Long toId) {
        return linehaulRouteService.findBetweenWarehousesWithDetails(fromId, toId);
    }

    @RequestMapping(value = {"/between-warehouses/as-locations/with-available-expeditions"}, method = RequestMethod.GET)
    public List<RouteResponse> findAllForWarehousesAsLocations(@RequestParam(required = false) Long fromId, @RequestParam(required = false) Long toId) {
        return linehaulRouteService.findBetweenWarehousesWithDetailsAsLocations(fromId, toId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public LinehaulRoute find(@PathVariable Long id) {
        return linehaulRouteService.findWithDetailsByIdOrThrowResourceNotFoundException(id);
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.POST)
    public LinehaulRoute create(@RequestBody LinehaulRoute route) {

        if (route != null && route.getId() != null) {
            throw new BadRequestException("This method must be used for creation.");
        }

        return linehaulRouteService.createOrUpdate(route);
    }

    @RequestMapping(value = {"/{id}", "/{id}/"}, method = RequestMethod.PUT)
    public LinehaulRoute update(@PathVariable Long id, @RequestBody LinehaulRoute route) {

        if (!id.equals(route.getId())) {
            throw new BadRequestException("LinehaulRoute.id must be " + id + ".");
        }

        return linehaulRouteService.createOrUpdate(route);
    }

    @RequestMapping(value = {"/{id}", "/{id}/"}, method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        linehaulRouteService.softDelete(id);
    }
}
