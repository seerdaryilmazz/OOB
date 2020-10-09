package ekol.crm.history.controller;

import ekol.crm.history.domain.Change;
import ekol.crm.history.domain.History;
import ekol.crm.history.domain.dto.ChangeJson;
import ekol.crm.history.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/history")
public class HistoryController {

    private HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService){
        this.historyService = historyService;
    }

    @RequestMapping(value="/changes",  method= RequestMethod.GET)
    public List<ChangeJson> retrieveChanges(@RequestParam Long id, @RequestParam String type) {
        List<Change> changes = historyService.retrieveChanges(id, type);
        return changes.stream().map(ChangeJson::fromEntity).collect(Collectors.toList());
    }

    @RequestMapping(value ="/latestData", method = RequestMethod.GET)
    public History retrieveLatestData(@RequestParam Long id, @RequestParam String type) {
        return historyService.retrieveLatestData(id,type);
    }
}

