package ekol.orders.lookup.controller;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.orders.lookup.domain.HSCode;
import ekol.orders.lookup.repository.HSCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by burak on 08/08/16.
 */
@RestController
@RequestMapping("/lookup/hscode")
public class HSCodeController extends BaseLookupApiController<HSCode> {

    @Autowired
    private HSCodeRepository hsCodeRepository;

    @PostConstruct
    public void init(){
        setLookupRepository(hsCodeRepository);
    }
}
