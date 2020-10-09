package ekol.orders.lookup.controller;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.orders.lookup.domain.AdrClass;
import ekol.orders.lookup.repository.AdrClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by kilimci on 05/08/16.
 */
@RestController
@RequestMapping("/lookup/adr-class")
public class AdrClassController extends BaseLookupApiController<AdrClass> {

    @Autowired
    private AdrClassRepository adrClassRepository;

    @PostConstruct
    public void init(){
        setLookupRepository(adrClassRepository);
    }

}
