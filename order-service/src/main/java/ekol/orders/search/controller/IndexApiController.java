package ekol.orders.search.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ekol.orders.search.service.OrderIndexService;
import lombok.AllArgsConstructor;

/**
 * Created by kilimci on 25/03/16.
 */
@RestController
@RequestMapping("/index")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class IndexApiController {

    private OrderIndexService orderIndexService;


    @GetMapping(value={"/order/{id}"})
    public void indexOrder(@PathVariable Long id){
    	orderIndexService.indexOrder(id);
    }
    
    @PostMapping(value={"/order"})
    public void indexOrder(@RequestBody List<String> codes){
    	codes.forEach(orderIndexService::indexOrder);
    }
    
    @GetMapping(value={"/shipment/{id}"})
    public void indexShipment(@PathVariable Long id){
        orderIndexService.indexShipment(id);
    }

    @GetMapping(value={"/shipment"})
    public void indexShipment(){
    	orderIndexService.reindexShipments();
    }
    
    @PostMapping(value={"/shipment"})
    public void indexShipment(@RequestBody List<String> codes){
    	codes.forEach(orderIndexService::indexShipment);
    }

}
