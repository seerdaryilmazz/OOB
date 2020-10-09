package ekol.location.controller;

import ekol.location.domain.RouteLegExpedition;
import ekol.location.service.RouteLegExpeditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by burak on 11/12/17.
 */
@RestController
@RequestMapping("/route-leg-expedition")
public class RouteLegExpeditionController {

    @Autowired
    private RouteLegExpeditionService routeLegExpeditionService;

    @RequestMapping(value = "/generate", method = RequestMethod.GET)
    public void generateRouteLegExpeditions() {
        routeLegExpeditionService.generateExpeditionsForTwoWeeks();
    }

    @RequestMapping(value = "/{expeditionId}", method = RequestMethod.GET)
    public RouteLegExpedition findById(@PathVariable Long expeditionId) {
        return routeLegExpeditionService.findById(expeditionId);
    }

    @RequestMapping(value = "/by-route-leg/{routeLegId}", method = RequestMethod.GET)
    public List<RouteLegExpedition> findById(@PathVariable Long routeLegId,
                                             @RequestParam (required = false) Boolean includeHistory,
                                             @RequestParam (required = false) String departureDateFrom,
                                             @RequestParam (required = false) String departureDateTo) {
        return routeLegExpeditionService.findByRouteLegId(routeLegId, includeHistory == null ? false : includeHistory, departureDateFrom, departureDateTo);
    }

}
