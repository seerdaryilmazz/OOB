package ekol.orders.transportOrder.controller;


import ekol.orders.transportOrder.domain.TradeType;
import ekol.orders.transportOrder.service.TradeTypeService;
import ekol.resource.controller.BaseEnumApiController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/trade-type")
public class TradeTypeApiController extends BaseEnumApiController<TradeType> {

    @Autowired
    private TradeTypeService tradeTypeService;

    @PostConstruct
    public void init() {
        setType(TradeType.class);
    }

    @RequestMapping(
            value = {"/by-src-and-dest-location", "/by-src-and-dest-location/"},
            method = RequestMethod.GET)
    private TradeType retrieveTradeTypeBySrcAndDestLocation(
            @RequestParam String sourceLocationId, @RequestParam String destinationLocationId) {

        return tradeTypeService.retrieveTradeType(sourceLocationId, destinationLocationId);
    }
}
