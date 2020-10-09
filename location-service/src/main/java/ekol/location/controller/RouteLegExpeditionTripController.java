package ekol.location.controller;

import ekol.location.domain.RouteLegExpeditionTrip;
import ekol.location.service.RouteLegExpeditionTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by burak on 11/12/17.
 */
@RestController
@RequestMapping("/route-leg-expedition-trip")
public class RouteLegExpeditionTripController {

    @Autowired
    private RouteLegExpeditionTripService routeLegExpeditionTripService;

    @RequestMapping(value = "/by-expedition-id/{expeditionId}", method = RequestMethod.GET)
    public List<RouteLegExpeditionTrip> findById(@PathVariable Long expeditionId) {
        return routeLegExpeditionTripService.findByExpeditionId(expeditionId);
    }

}
