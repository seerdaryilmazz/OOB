package ekol.orders.transportOrder.controller;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.orders.transportOrder.domain.TransportType;
import ekol.orders.transportOrder.repository.TransportTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/transport-type")
public class TransportTypeApiController extends BaseLookupApiController<TransportType> {

    @Autowired
    private TransportTypeRepository transportTypeRepository;

    @PostConstruct
    public void init() {
        setLookupRepository(transportTypeRepository);
    }
}
