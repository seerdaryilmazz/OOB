package ekol.orders.lookup.controller;

import ekol.hibernate5.domain.controller.BaseLookupApiController;
import ekol.orders.lookup.domain.AdrPackageType;
import ekol.orders.lookup.repository.AdrPackageTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/lookup/adr-package-type")
public class AdrPackageTypeController extends BaseLookupApiController<AdrPackageType> {

    @Autowired
    private AdrPackageTypeRepository packageTypeRepository;

    @PostConstruct
    public void init(){
        setLookupRepository(packageTypeRepository);
    }
}
